In my case found that it was YARN ACL problem. So the following helped me:

yarn.acl.enable = false
or
yarn.admin.acl = activity_analyzer,yarn,dr.who,admin
As a bonus, gathered full configuration for HDP 3.1 + TEZ 0.9.2:

YARN configuration:

yarn.timeline-service.enabled = true
yarn.acl.enable = false
yarn.admin.acl = activity_analyzer,yarn,dr.who,admin
yarn.timeline-service.webapp.address = <host>:8188
yarn.timeline-service.version = 2,0f
yarn.timeline-service.hostname = <host>
yarn.timeline-service.http-cross-origin.enabled = true
yarn.timeline-service.http-cross-origin.allowed-origins = *
yarn.resourcemanager.system-metrics-publisher.enabled = true
yarn.timeline-service.entity-group-fs-store.group-id-plugin-classes = org.apache.tez.dag.history.logging.ats.TimelineCachePluginImpl
TEZ configuration:

yarn.timeline-service.enabled = true
tez.tez-ui.history-url.base = http://<host>/tez-ui/
tez.am.tez-ui.history-url.template = __HISTORY_URL_BASE__?viewPath=/#/tez-app/__APPLICATION_ID__
tez.history.logging.service.class = org.apache.tez.dag.history.logging.ats.ATSV15HistoryLoggingService
tez.dag.history.logging.enabled = true
tez.am.history.logging.enabled = true
tez.allow.disabled.timeline-domains = true
Hive configuration:

hive_timeline_logging_enabled = true
hive.exec.pre.hooks = org.apache.hadoop.hive.ql.hooks.ATSHook
hive.exec.post.hooks = org.apache.hadoop.hive.ql.hooks.ATSHook,org.apache.atlas.hive.hook.HiveHook
hive.exec.failure.hooks = org.apache.hadoop.hive.ql.hooks.ATSHook
HDFS configuration:

hadoop.http.filter.initializers = org.apache.hadoop.security.HttpCrossOriginFilterInitializer
