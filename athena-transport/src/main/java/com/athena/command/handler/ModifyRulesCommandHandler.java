package com.athena.command.handler;

import com.athena.command.CommandHandler;
import com.athena.command.CommandRequest;
import com.athena.command.CommandResponse;
import com.athena.annotation.CommandMapping;
import com.athena.datasource.WritableDataSource;
import com.athena.log.RecordLog;
import com.athena.util.StringUtil;
import java.net.URLDecoder;
import java.util.List;

/**
 * @author mukong
 */
@CommandMapping(name = "setRules", desc = "modify the rules, accept param: type={ruleType}&data={ruleJson}")
public class ModifyRulesCommandHandler implements CommandHandler<String> {

    @Override
    public CommandResponse<String> handle(CommandRequest request) {
        String type = request.getParam("type");
        // rule data in get parameter
        String data = request.getParam("data");
        if (StringUtil.isNotEmpty(data)) {
            try {
                data = URLDecoder.decode(data, "utf-8");
            } catch (Exception e) {
                RecordLog.info("Decode rule data error", e);
                return CommandResponse.ofFailure(e, "decode rule data error");
            }
        }

        RecordLog.info(String.format("Receiving rule change (type: %s): %s", type, data));

        String result = "success";

        if (FLOW_RULE_TYPE.equalsIgnoreCase(type)) {
//            List<FlowRule> flowRules = JSONArray.parseArray(data, FlowRule.class);
//            FlowRuleManager.loadRules(flowRules);
//            if (!writeToDataSource(getFlowDataSource(), flowRules)) {
//                result = WRITE_DS_FAILURE_MSG;
//            }
            return CommandResponse.ofSuccess(result);
        }

        return CommandResponse.ofFailure(new IllegalArgumentException("invalid type"));
    }

    /**
     * Write target value to given data source.
     *
     * @param dataSource writable data source
     * @param value target value to save
     * @param <T> value type
     * @return true if write successful or data source is empty; false if error occurs
     */
    private <T> boolean writeToDataSource(WritableDataSource<T> dataSource, T value) {
        if (dataSource != null) {
            try {
                dataSource.write(value);
            } catch (Exception e) {
                RecordLog.warn("Write data source failed", e);
                return false;
            }
        }
        return true;
    }

    private static final String WRITE_DS_FAILURE_MSG = "partial success (write data source failed)";
    private static final String FLOW_RULE_TYPE = "flow";
    private static final String DEGRADE_RULE_TYPE = "degrade";
    private static final String SYSTEM_RULE_TYPE = "system";
    private static final String AUTHORITY_RULE_TYPE = "authority";
}
