package com.sendtomoon.dgg.server;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.annotation.PostConstruct;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.cache.ChildData;
import org.apache.curator.framework.recipes.cache.TreeCache;
import org.apache.curator.framework.recipes.cache.TreeCacheEvent;
import org.drools.compiler.kie.builder.impl.InternalKieModule;
import org.kie.api.KieServices;
import org.kie.api.builder.KieRepository;
import org.kie.api.builder.ReleaseId;
import org.kie.api.runtime.Environment;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
@Service
@RestController
public class DroolsService {
	private static final Logger LOG = LoggerFactory.getLogger(DroolsService.class);

    private static final String GROUP_NAME = "com.cyzy";
    private static final String VERSION = "1.0.0";
    private static final String ZK_ENABLED = "spring.cloud.zookeeper.enabled";
    private static final String ZK_PRIFEX_NODE_PATH = "/drools.rules.test";
    private static final String ZK_NODE_TEST_VALUE = "0";
    private static final String PRIFEX_DIR="drools.prefixDir";
    private static final String TARGET_DIR="drools.targetDir";
    private static final String RELOAD_RULES_OK = "reload drools's rules ok......";

    @Autowired
    private Environment env;

    @Autowired
    private ApplicationContext appCtx;

    private CuratorFramework client;

    private KieServices kieServices;

    private KieRepository repository;

    private ConcurrentHashMap<String, KieContainer> kieContainers = new ConcurrentHashMap<String, KieContainer>();

    @PostConstruct
    protected void initKieContainer() throws Exception {
        kieServices = KieServices.Factory.get();
        repository = kieServices.getRepository();

        boolean isZKEnabled = env.getProperty(ZK_ENABLED, Boolean.class);
        if (isZKEnabled) {
            client = appCtx.getBean(CuratorFramework.class);
            loadRulesFromZK();
        } else {
            loadConfigFromLocalFile();
        }
    }

    /**
     * 从ZK加载规则
     * @Title: loadRulesFromZK 
     * @throws Exception
     * @return: void
     */
    private void loadRulesFromZK() throws Exception{
        ZkNodeListenerAdapter.getInstance()
        //设置watcher
        .watcher(new ZKNodeWatcher(ZK_PRIFEX_NODE_PATH, client))
        //默认处理器
        .setDefaultHandler(new ZkNodeHandler() {
            @Override
            public String execute(CuratorFramework client, TreeCache tc, TreeCacheEvent event, ChildData targetChildData,
                    Map<String, ChildData> filterChildren) {
                String msg = RELOAD_RULES_OK;
                try {
                    String pathName = genPathName(event.getData().getPath());
                    loadRules(pathName, getRulesFromZKNodes(pathName, filterChildren));
                } catch (Exception e) {
                    msg = e.getMessage();
                    LOG.error(msg);
                }
                return msg;
            }
        })
        //a_push_node节点值为0时的处理器
        .addHandler(ZK_NODE_TEST_VALUE, new ZkNodeHandler() {
            @Override
            public String execute(CuratorFramework client, TreeCache tc, TreeCacheEvent event, ChildData targetChildData,
                    Map<String, ChildData> filterChildren) {
                String msg = RELOAD_RULES_OK;
                try {
                    String pathName = genPathName(event.getData().getPath());
                    testRules(pathName, getRulesFromZKNodes(pathName, filterChildren));
                } catch (Exception e) {
                    msg = e.getMessage();
                    LOG.error(msg);
                }
                return msg;
            }
        })
        //增加此监听器到watcher
        .addToWatcher(ZK_PRIFEX_NODE_PATH+"/gps/zte")
        .addToWatcher(ZK_PRIFEX_NODE_PATH+"/gps/zte2")
        .addToWatcher(ZK_PRIFEX_NODE_PATH+"/group1")
        .addToWatcher(ZK_PRIFEX_NODE_PATH+"/group2")
        //启动监听
        .start();
    }

    /**
     * 从本地文件加载规则
     * @Title: loadConfigFromLocalFile 
     * @throws Exception
     * @return: void
     */
    private void loadConfigFromLocalFile() throws Exception{
        String targetDir = env.getProperty(TARGET_DIR);
        loadRules(targetDir, getRulesFromLocalFile(targetDir));
    }

    /**
     * 正式加载规则文件进行使用
     */
    private void loadRules(String ruleInZKPath, List<ResourceWrapper> resourceWrappers) throws Exception {
        // if failed throws Exception+
        ReleaseId releaseId = kieServices.newReleaseId(GROUP_NAME, ruleInZKPath, VERSION);
        InternalKieModule kieModule = DroolsUtils.createKieModule(kieServices, releaseId, resourceWrappers);
        // if succeed will add new module
        repository.addKieModule(kieModule);
        KieContainer kieContainer = kieServices.newKieContainer(releaseId);
        kieContainer.updateToVersion(releaseId);
        kieContainers.put(ruleInZKPath, kieContainer);
    }

    /**
     * 测试加载规则文件
     */
    private void testRules(String ruleInZKPath, List<ResourceWrapper> resourceWrappers) throws Exception{
        ReleaseId releaseId = kieServices.newReleaseId(GROUP_NAME, ruleInZKPath, VERSION);
        DroolsUtils.createKieModule(kieServices, releaseId, resourceWrappers);
    }

    /**
     * 从ENV配置的指定文件夹中获取规则文件
     */
    private List<ResourceWrapper> getRulesFromLocalFile(String path) {
        String prefixDir = env.getProperty(PRIFEX_DIR);
        String targetDir = env.getProperty(TARGET_DIR);
        List<ResourceWrapper> resourceWrappers = new ArrayList<ResourceWrapper>();
        List<File> ruleFiles = new ArrayList<File>();
        try {
            ruleFiles = DroolsUtils.getDefaultRuleFiles(prefixDir, targetDir);
        } catch (Exception e) {}
        if(ruleFiles.isEmpty()){
            throw new RuntimeException("can't load rules from " + prefixDir +"/"+ targetDir);
        }
        LOG.info("################################load ["+path+"] rules################################");
        for (File file : ruleFiles) {
            LOG.info(file.getName());
            resourceWrappers.add(new ResourceWrapper(ResourceFactory.newFileResource(file), file.getName()));
        }
        LOG.info("################################load ["+path+"] rules################################");
        return resourceWrappers;
    }

    /**
     * 从ZK节点上获取规则文件
     */
    private List<ResourceWrapper> getRulesFromZKNodes(String path, Map<String, ChildData> filterChildren) {
        List<ResourceWrapper> resourceWrappers = new ArrayList<ResourceWrapper>();
        LOG.info("################################load ["+path+"] rules################################");
        for (Entry<String, ChildData> entry : filterChildren.entrySet()) {
            LOG.info(entry.getKey());
            resourceWrappers.add(
                    new ResourceWrapper(ResourceFactory.newByteArrayResource(entry.getValue().getData()),entry.getKey()));
        }
        LOG.info("################################load ["+path+"] rules################################");
        return resourceWrappers;
    }

    private String genPathName(String zkNodePath) {
        String name = "";
        Matcher matcher = Pattern.compile("(.*?)/a_push_node").matcher(zkNodePath);
        if (matcher.matches()) {
            name = matcher.group(1);
        }
        return name;
    }

    /**
     * 创建指定ZK节点路径下的KieSession
     * @Title: newSession 
     * @Description: TODO
     * @param ruleInZKPath
     * @return: KieSession
     */
    private KieSession newSession(String ruleInZKPath) {
        KieContainer kieContainer = kieContainers.get(ruleInZKPath);
        if(kieContainer==null){
            throw new RuntimeException("can't get KieContainer with the name:" + ruleInZKPath);
        }
        KieSession session = kieContainer.newKieSession();
        // 默认配置
        session.setGlobal("appCtx", appCtx);
        return session;
    }

    /**
     * 执行指定ZK节点路径下的所有“MAIN”议程组(使用agenda-group定义，默认是MAIN)的规则
     * @Title: fireAllRules 
     * @param ruleInZKPath
     * @param facts
     * @return: void
     */
    public void fireMainGroupRules(String ruleInZKPath, Object... facts) {
        fireRules(ruleInZKPath, null, null, facts);
    }

    /**
     * 执行指定ZK节点路径下的所有“MAIN”议程组(使用agenda-group定义，默认是MAIN)的并且经过AgendaFilter过滤的规则
     * @Title: fireAllRules 
     * @param ruleInZKPath
     * @param filter
     * @param facts
     * @return: void
     */
    public void fireMainGroupRules(String ruleInZKPath, AgendaFilter filter, Object... facts) {
        fireRules(ruleInZKPath, null, filter, facts);
    }

    /**
     * 执行指定ZK节点路径下的所有agendaGroup指定议程组和“MAIN”议程组(使用agenda-group定义，默认是MAIN)的并且经过AgendaFilter过滤的规则
     * @Title: fireAllRules 
     * @param ruleInZKPath 规则所在ZK node的路径
     * @param agendaGroup 规则所在议程组名称，如果不定义默认：MAIN
     * @param filter 规则过滤器
     * @param facts 事实
     * @return: void
     */
    public void fireRules(String ruleInZKPath, String agendaGroup, AgendaFilter filter, Object... facts) {
        KieSession session = null;
        try {
            session = newSession(ruleInZKPath);
            // add fact
            for (Object fact : facts) {
                session.insert(fact);
            }
            //focus agenda group
            if (agendaGroup != null && !agendaGroup.isEmpty()) {
                session.getAgenda().getAgendaGroup(agendaGroup).setFocus();
            }
            // add filter
            if (filter != null) {
                session.fireAllRules(filter);
            } else {
                session.fireAllRules();
            }
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        } finally {
            if (session != null) {
                session.dispose();
            }
        }
    }

    @RequestMapping(value = "/app/drools/reload", method = RequestMethod.GET)
    public @ResponseBody ResponseEntity<String> reloadRules() {
        String msg = RELOAD_RULES_OK;
        try {
            String targetDir = env.getProperty(TARGET_DIR);
            loadRules(targetDir, getRulesFromLocalFile(targetDir));
        } catch (Exception e) {
            msg = e.getMessage();
        }
        return ResponseEntity.ok().body(msg);
    }

    @RequestMapping(value = "/app/drools/test", method = RequestMethod.GET)
    public @ResponseBody ResponseEntity<String> testRules() {
        String msg = RELOAD_RULES_OK;
        try {
            String targetDir = env.getProperty(TARGET_DIR);
            testRules(targetDir, getRulesFromLocalFile(targetDir));
        } catch (Exception e) {
            msg = e.getMessage();
        }
        return ResponseEntity.ok().body(msg);
    }

    @RequestMapping(value = "/app/drools/test2", method = RequestMethod.GET)
    public @ResponseBody ResponseEntity<String> test2() {
        String msg = "test ok";
        try {
            fireRules(ZK_PRIFEX_NODE_PATH + "/group1", "group1", null, new Object());
            fireRules(ZK_PRIFEX_NODE_PATH + "/group2", "", null, new Object());

            fireRules(ZK_PRIFEX_NODE_PATH + "/gps/zte", "gps.zte", null, new Object());
            fireRules(ZK_PRIFEX_NODE_PATH + "/gps/zte2", "gps.zte", null, new Object());

//          fireAllRules("rules", null, null, new Object());
        } catch (Exception e) {
            msg = e.getMessage();
        }
        return ResponseEntity.ok().body(msg);

    }

}
