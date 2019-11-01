package com.ying.ql;

import com.ql.util.express.ArraySwap;
import com.ql.util.express.InstructionSetContext;
import com.ql.util.express.OperateData;
import com.ql.util.express.instruction.op.OperatorBase;

public class OperatorContextPut extends OperatorBase {

    public OperatorContextPut(String aName) {
        this.name = aName;
    }

    @Override
    public OperateData executeInner(InstructionSetContext parent, ArraySwap list) throws Exception {
        String key = list.get(0).toString();
        Object value = list.get(1);
        parent.put(key, value);
        return null;
    }
}
