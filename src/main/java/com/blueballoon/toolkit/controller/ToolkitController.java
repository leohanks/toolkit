package com.blueballoon.toolkit.controller;

import com.blueballoon.toolkit.exception.ServiceException;
import com.blueballoon.toolkit.service.ToolService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;

/**
 *  实用工具接口层
 *
 * @author hanzhen
 * @since  2019 /11/14
 */
@RestController
@RequestMapping(value = "/tool/")
public class ToolkitController {
    @Autowired
    private ToolService toolsService;
    
    private static final String INVALID_PARAM = "非法参数";

    /**
     * 单组均值与固定值比较
     *
     * @param difference          样本均值与固定值（总体均值）比较的差
     * @param std                 总体标准差
     * @param meanAndConstLateral 单双侧
     * @param exam_a              检验水准a
     * @param exam_b              检验水准b
     * @return string
     */
    @GetMapping(value = "compareMeanAndConst", produces = {"text/html; charset=GB2312"})
    public String compareMeanAndConst(
            @RequestParam(value = "difference", required = true, defaultValue = "0") BigDecimal difference,
            @RequestParam(value = "std", required = true, defaultValue = "0") BigDecimal std,
            @RequestParam(value = "meanAndConstLateral", required = true, defaultValue = "0") Integer meanAndConstLateral,
            @RequestParam(value = "exam_a", required = true, defaultValue = "0") BigDecimal exam_a,
            @RequestParam(value = "exam_b", required = true, defaultValue = "0") BigDecimal exam_b) {
        try {
            return toolsService.compareMeanAndConst(difference, std, meanAndConstLateral, exam_a, exam_b);
        } catch (ServiceException e) {
            return INVALID_PARAM;
        }
    }

    /**
     * 两独立样本均值比较
     *
     * @param uT                     实验组均值
     * @param uC                     对照组均值
     * @param combineStd             合并后标准差
     * @param independenceLateral    单双侧
     * @param independenceMeanExam_a 检验水准a
     * @param independenceMeanExam_b 检验水准b
     * @param independenceMeanR      试验组与对照组样本量之比
     * @return string
     */
    @GetMapping(value = "compareIndependenceMean", produces = {"text/html; charset=GB2312"})
    public String compareIndependenceMean(
            @RequestParam(value = "uT", required = true, defaultValue = "0") BigDecimal uT,
            @RequestParam(value = "uC", required = true, defaultValue = "0") BigDecimal uC,
            @RequestParam(value = "combineStd", required = true, defaultValue = "0") BigDecimal combineStd,
            @RequestParam(value = "independenceLateral", required = true, defaultValue = "0") Integer independenceLateral,
            @RequestParam(value = "independenceMeanExam_a", required = true, defaultValue = "0") BigDecimal independenceMeanExam_a,
            @RequestParam(value = "independenceMeanExam_b", required = true, defaultValue = "0") BigDecimal independenceMeanExam_b,
            @RequestParam(value = "independenceMeanR", required = true, defaultValue = "0") BigDecimal independenceMeanR
    ) {
        try {
            return toolsService.compareIndependenceMean(uT, uC, combineStd, independenceLateral, independenceMeanExam_a, independenceMeanExam_b, independenceMeanR);
        } catch (ServiceException e) {
            return INVALID_PARAM;
        }
    }

    /**
     * 两组独立样本均值的非劣效检验
     *
     * @param testGroupMean            试验组均值
     * @param controlGroupMean         对照组均值
     * @param combineStdNonInferiority 合并后标准差
     * @param dividingValue            非劣效戒指
     * @param nonInferiority_a         检验水准a
     * @param nonInferiority_b         检验水准b
     * @param nonInferiorityR          试验组与对照组样本量之比r
     * @return string
     */
    @GetMapping(value = "compareNonInferiority", produces = {"text/html; charset=GB2312"})
    public String compareNonInferiority(
            @RequestParam(value = "testGroupMean", required = true, defaultValue = "0") BigDecimal testGroupMean,
            @RequestParam(value = "controlGroupMean", required = true, defaultValue = "0") BigDecimal controlGroupMean,
            @RequestParam(value = "combineStdNonInferiority", required = true, defaultValue = "0") BigDecimal combineStdNonInferiority,
            @RequestParam(value = "dividingValue", required = true, defaultValue = "0") BigDecimal dividingValue,
            @RequestParam(value = "nonInferiority_a", required = true, defaultValue = "0") BigDecimal nonInferiority_a,
            @RequestParam(value = "nonInferiority_b", required = true, defaultValue = "0") BigDecimal nonInferiority_b,
            @RequestParam(value = "nonInferiorityR", required = true, defaultValue = "0") BigDecimal nonInferiorityR
    ) {
        try {
            return toolsService.compareNonInferiority(testGroupMean, controlGroupMean, combineStdNonInferiority, dividingValue, nonInferiority_a, nonInferiority_b, nonInferiorityR);
        } catch (ServiceException e) {
            return INVALID_PARAM;
        }
    }

    /**
     * 两组独立样本均值的优效检验
     *
     * @param superiorityTestTestGroupMean            试验组均值
     * @param superiorityTestControlGroupMean         对照组均值
     * @param superiorityTestCombineStdNonInferiority 合并标准差
     * @param superiorityTestDividingValue            优效界值
     * @param superiorityTest_a                       检验水准a
     * @param superiorityTest_b                       检验水准b
     * @param superiorityTestR                        试验组与对照组样本量之比
     * @return string
     */
    @GetMapping(value = "compareSuperiorityTest", produces = {"text/html; charset=GB2312"})
    public String compareSuperiorityTest(
            @RequestParam(value = "superiorityTestTestGroupMean", required = true, defaultValue = "0") BigDecimal superiorityTestTestGroupMean,
            @RequestParam(value = "superiorityTestControlGroupMean", required = true, defaultValue = "0") BigDecimal superiorityTestControlGroupMean,
            @RequestParam(value = "superiorityTestCombineStdNonInferiority", required = true, defaultValue = "0") BigDecimal superiorityTestCombineStdNonInferiority,
            @RequestParam(value = "superiorityTestDividingValue", required = true, defaultValue = "0") BigDecimal superiorityTestDividingValue,
            @RequestParam(value = "superiorityTest_a", required = true, defaultValue = "0") BigDecimal superiorityTest_a,
            @RequestParam(value = "superiorityTest_b", required = true, defaultValue = "0") BigDecimal superiorityTest_b,
            @RequestParam(value = "superiorityTestR", required = true, defaultValue = "0") BigDecimal superiorityTestR
    ) {
        try {
            return toolsService.compareSuperiorityTest(superiorityTestTestGroupMean, superiorityTestControlGroupMean, superiorityTestCombineStdNonInferiority, superiorityTestDividingValue, superiorityTest_a, superiorityTest_b, superiorityTestR);
        } catch (ServiceException e) {
            return INVALID_PARAM;
        }
    }

    /**
     * 单组率与固定值比较
     *
     * @param ratioAndConstant_testGroup_pai_1 试验组的率 Π1
     * @param ratioAndConstant_base_pai_0      基线率Π0
     * @param ratioAndConstant_lateral         单双侧
     * @param ratioAndConstant_a               检验水准a
     * @param ratioAndConstant_b               检验水准b
     * @return string
     */
    @GetMapping(value = "compareRatioAndConstant", produces = {"text/html; charset=GB2312"})
    public String compareRatioAndConstant(
            @RequestParam(value = "ratioAndConstant_testGroup_pai_1", required = true, defaultValue = "0") BigDecimal ratioAndConstant_testGroup_pai_1,
            @RequestParam(value = "ratioAndConstant_base_pai_0", required = true, defaultValue = "0") BigDecimal ratioAndConstant_base_pai_0,
            @RequestParam(value = "ratioAndConstant_lateral", required = true, defaultValue = "0") Integer ratioAndConstant_lateral,
            @RequestParam(value = "ratioAndConstant_a", required = true, defaultValue = "0") BigDecimal ratioAndConstant_a,
            @RequestParam(value = "ratioAndConstant_b", required = true, defaultValue = "0") BigDecimal ratioAndConstant_b
    ) {
        try {
            return toolsService.compareRatioAndConstant(ratioAndConstant_testGroup_pai_1, ratioAndConstant_base_pai_0, ratioAndConstant_lateral, ratioAndConstant_a, ratioAndConstant_b);
        } catch (ServiceException e) {
            return INVALID_PARAM;
        }
    }

    /**
     * 两组独立样本率比较
     *
     * @param independenceRatio_testGroup_pi_T    试验组的率
     * @param independenceRatio_controlGroup_pi_C 对照组的率
     * @param independenceRatio_lateral           单双侧
     * @param independenceRatio_a                 检验水准a
     * @param independenceRatio_b                 检验水准b
     * @param independenceRatioR                  试验组与对照组样本量之比r
     * @return string
     */
    @GetMapping(value = "compareIndependenceRatio", produces = {"text/html; charset=GB2312"})
    public String compareIndependenceRatio(
            @RequestParam(value = "independenceRatio_testGroup_pi_T", required = true, defaultValue = "0") BigDecimal independenceRatio_testGroup_pi_T,
            @RequestParam(value = "independenceRatio_controlGroup_pi_C", required = true, defaultValue = "0") BigDecimal independenceRatio_controlGroup_pi_C,
            @RequestParam(value = "independenceRatio_lateral", required = true, defaultValue = "0") Integer independenceRatio_lateral,
            @RequestParam(value = "independenceRatio_a", required = true, defaultValue = "0") BigDecimal independenceRatio_a,
            @RequestParam(value = "independenceRatio_b", required = true, defaultValue = "0") BigDecimal independenceRatio_b,
            @RequestParam(value = "independenceRatioR", required = true, defaultValue = "0") BigDecimal independenceRatioR
    ) {
        try {
            return toolsService.compareIndependenceRatio(independenceRatio_testGroup_pi_T, independenceRatio_controlGroup_pi_C, independenceRatio_lateral, independenceRatio_a, independenceRatio_b, independenceRatioR);
        } catch (ServiceException e) {
            return INVALID_PARAM;
        }
    }

    /**
     * 两组独立样本率的非劣效检验
     *
     * @param irni_testGroup_pi_T    试验组的率 ΠT
     * @param irni_controlGroup_pi_C 对照组的率 ΠC
     * @param irniDividingValue      非劣效界值
     * @param irni_a                 单侧检验水准a
     * @param irni_b                 检验水准b
     * @param irniR                  试验组与对对照组样本量之比 r
     * @return string
     */
    @GetMapping(value = "compareIndependenceRatioNonInferiority", produces = {"text/html; charset=GB2312"})
    public String compareIndependenceRatioNonInferiority(
            @RequestParam(value = "irni_testGroup_pi_T", required = true, defaultValue = "0") BigDecimal irni_testGroup_pi_T,
            @RequestParam(value = "irni_controlGroup_pi_C", required = true, defaultValue = "0") BigDecimal irni_controlGroup_pi_C,
            @RequestParam(value = "irniDividingValue", required = true, defaultValue = "0") BigDecimal irniDividingValue,
            @RequestParam(value = "irni_a", required = true, defaultValue = "0") BigDecimal irni_a,
            @RequestParam(value = "irni_b", required = true, defaultValue = "0") BigDecimal irni_b,
            @RequestParam(value = "irniR", required = true, defaultValue = "0") BigDecimal irniR
    ) {
        try {
            return toolsService.compareIndependenceRatioNonInferiority(irni_testGroup_pi_T, irni_controlGroup_pi_C, irniDividingValue, irni_a, irni_b, irniR);
        } catch (ServiceException e) {
            return INVALID_PARAM;
        }
    }

    /**
     * 两组独立样本率的优效检验
     *
     * @param irst_testGroup_pi_T    试验组的率 ΠT
     * @param irst_controlGroup_pi_C 对照组的率 ΠC
     * @param irstDividingValue      非劣效界值
     * @param irst_a                 单侧检验水准a
     * @param irst_b                 检验水准b
     * @param irstR                  试验组与对对照组样本量之比 r
     * @return string
     */
    @GetMapping(value = "compareIndependenceRatioSuperiorityTest", produces = {"text/html; charset=GB2312"})
    public String compareIndependenceRatioSuperiorityTest(
            @RequestParam(value = "irst_testGroup_pi_T", required = true, defaultValue = "0") BigDecimal irst_testGroup_pi_T,
            @RequestParam(value = "irst_controlGroup_pi_C", required = true, defaultValue = "0") BigDecimal irst_controlGroup_pi_C,
            @RequestParam(value = "irstDividingValue", required = true, defaultValue = "0") BigDecimal irstDividingValue,
            @RequestParam(value = "irst_a", required = true, defaultValue = "0") BigDecimal irst_a,
            @RequestParam(value = "irst_b", required = true, defaultValue = "0") BigDecimal irst_b,
            @RequestParam(value = "irstR", required = true, defaultValue = "0") BigDecimal irstR
    ) {
        try {
            return toolsService.compareIndependenceRatioSuperiorityTest(irst_testGroup_pi_T, irst_controlGroup_pi_C, irstDividingValue, irst_a, irst_b, irstR);
        } catch (ServiceException e) {
            return INVALID_PARAM;
        }
    }

    /**
     * 以分类变量（率）估计样本量
     *
     * @param ssbcv_Sensibility 灵敏度/特异度的预期值
     * @param ssbcv_ErrorRange  灵敏度估计误差范围
     * @param ssbcv_lateral     单双侧
     * @param ssbcv_a           检验水准 a
     * @return string
     */
    @GetMapping(value = "estimationSamplSizeByClassifiedVariable", produces = {"text/html; charset=GB2312"})
    public String estimationSamplSizeByClassifiedVariable(
            @RequestParam(value = "ssbcv_Sensibility", required = true, defaultValue = "0") BigDecimal ssbcv_Sensibility,
            @RequestParam(value = "ssbcv_ErrorRange", required = true, defaultValue = "0") BigDecimal ssbcv_ErrorRange,
            @RequestParam(value = "ssbcv_lateral", required = true, defaultValue = "0") Integer ssbcv_lateral,
            @RequestParam(value = "ssbcv_a", required = true, defaultValue = "0") BigDecimal ssbcv_a
    ) {
        try {
            return toolsService.estimationSamplSizeByClassifiedVariable(ssbcv_Sensibility, ssbcv_ErrorRange, ssbcv_lateral, ssbcv_a);
        } catch (ServiceException e) {
            return INVALID_PARAM;
        }
    }
}
