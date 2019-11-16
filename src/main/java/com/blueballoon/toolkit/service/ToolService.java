package com.blueballoon.toolkit.service;

import java.math.BigDecimal;

/**
 * @author hanzhen
 * @description： 工具类，服务接口层
 * @date 2019/11/14
 */
public interface ToolService {

    /**
     * 单组均值与固定值比较
     * @param difference 样本均值与固定值（总体均值）比较的差
     * @param std 总体标准差
     * @param lateral 单双侧
     * @param exam_a 检验水准a
     * @param exam_b 检验水准b
     * @return
     */
    String compareMeanAndConst(BigDecimal difference, BigDecimal std, Integer lateral, BigDecimal exam_a, BigDecimal exam_b);

    /**
     * 两独立样本均值比较
     * @param uT 实验组均值
     * @param uC 对照组均值
     * @param combineStd 合并后标准差
     * @param lateral 单双侧
     * @param exam_a 检验水准a
     * @param exam_b 检验水准b
     * @param r 试验组与对照组样本量之比
     * @return
     */
    String compareIndependenceMean(BigDecimal uT, BigDecimal uC, BigDecimal combineStd, Integer lateral, BigDecimal exam_a, BigDecimal exam_b, BigDecimal r);

    /**
     * 两组独立样本均值的非劣效检验
     * @param testGroupMean 试验组均值
     * @param controlGroupMean 对照组均值
     * @param combineStdNonInferiority 合并后标准差
     * @param dividingValue 非劣效戒指
     * @param nonInferiority_a 检验水准a
     * @param nonInferiority_b 检验水准b
     * @param nonInferiorityR 试验组与对照组样本量之比r
     * @return
     */
    String compareNonInferiority(BigDecimal testGroupMean, BigDecimal controlGroupMean, BigDecimal combineStdNonInferiority, BigDecimal dividingValue, BigDecimal nonInferiority_a, BigDecimal nonInferiority_b, BigDecimal nonInferiorityR);

    /**
     * 两组独立样本均值的优效检验
     * @param superiorityTestTestGroupMean 试验组均值
     * @param superiorityTestControlGroupMean 对照组均值
     * @param superiorityTestCombineStdNonInferiority 合并标准差
     * @param superiorityTestDividingValue 优效界值
     * @param superiorityTest_a 检验水准a
     * @param superiorityTest_b 检验水准b
     * @param superiorityTestR 试验组与对照组样本量之比
     * @return
     */
    String compareSuperiorityTest(
            BigDecimal superiorityTestTestGroupMean,
            BigDecimal superiorityTestControlGroupMean,
            BigDecimal superiorityTestCombineStdNonInferiority,
            BigDecimal superiorityTestDividingValue,
            BigDecimal superiorityTest_a,
            BigDecimal superiorityTest_b,
            BigDecimal superiorityTestR
    );

    /**
     * 单组率与固定值比较
     * @param ratioAndConstant_testGroup_pai_1 试验组的率 Π1
     * @param ratioAndConstant_base_pai_0 基线率Π0
     * @param ratioAndConstant_lateral 单双侧
     * @param ratioAndConstant_a 检验水准a
     * @param ratioAndConstant_b 检验水准b
     * @return
     */
    String compareRatioAndConstant(
            BigDecimal ratioAndConstant_testGroup_pai_1,
            BigDecimal ratioAndConstant_base_pai_0,
            Integer ratioAndConstant_lateral,
            BigDecimal ratioAndConstant_a,
            BigDecimal ratioAndConstant_b
    );

    /**
     * 两组独立样本率比较
     * @param independenceRatio_testGroup_pi_T 试验组的率
     * @param independenceRatio_controlGroup_pi_C 对照组的率
     * @param independenceRatio_lateral 单双侧
     * @param independenceRatio_a 检验水准a
     * @param independenceRatio_b 检验水准b
     * @param independenceRatioR 试验组与对照组样本量之比r
     * @return
     */
    String compareIndependenceRatio(
            BigDecimal independenceRatio_testGroup_pi_T,
            BigDecimal independenceRatio_controlGroup_pi_C,
            Integer independenceRatio_lateral,
            BigDecimal independenceRatio_a,
            BigDecimal independenceRatio_b,
            BigDecimal independenceRatioR
    );

    /**
     * 两组独立样本率的非劣效检验
     * @param irni_testGroup_pi_T 试验组的率 ΠT
     * @param irni_controlGroup_pi_C 对照组的率 ΠC
     * @param irniDividingValue 非劣效界值
     * @param irni_a 单侧检验水准a
     * @param irni_b 检验水准b
     * @param irniR 试验组与对对照组样本量之比 r
     * @return
     */
    String compareIndependenceRatioNonInferiority(
            BigDecimal irni_testGroup_pi_T,
            BigDecimal irni_controlGroup_pi_C,
            BigDecimal irniDividingValue,
            BigDecimal irni_a,
            BigDecimal irni_b,
            BigDecimal irniR
    );

    /**
     * 两组独立样本率的优效检验
     * @param irst_testGroup_pi_T 试验组的率 ΠT
     * @param irst_controlGroup_pi_C 对照组的率 ΠC
     * @param irstDividingValue 非劣效界值
     * @param irst_a 单侧检验水准a
     * @param irst_b 检验水准b
     * @param irstR 试验组与对对照组样本量之比 r
     * @return
     */
    String compareIndependenceRatioSuperiorityTest(
            BigDecimal irst_testGroup_pi_T,
            BigDecimal irst_controlGroup_pi_C,
            BigDecimal irstDividingValue,
            BigDecimal irst_a,
            BigDecimal irst_b,
            BigDecimal irstR
    );

    /**
     * 以分类变量（率）估计样本量
     * @param ssbcv_Sensibility 灵敏度/特异度的预期值
     * @param ssbcv_ErrorRange 灵敏度估计误差范围
     * @param ssbcv_lateral 单双侧
     * @param ssbcv_a 检验水准 a
     * @return
     */
    String estimationSamplSizeByClassifiedVariable(
            BigDecimal ssbcv_Sensibility,
            BigDecimal ssbcv_ErrorRange,
            Integer ssbcv_lateral,
            BigDecimal ssbcv_a
    );
}
