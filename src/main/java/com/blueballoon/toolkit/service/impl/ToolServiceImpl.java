package com.blueballoon.toolkit.service.impl;

import com.blueballoon.toolkit.exception.ServiceException;
import com.blueballoon.toolkit.service.ToolService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.math3.distribution.NormalDistribution;
import org.apache.commons.math3.exception.OutOfRangeException;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * @author hanzhen
 * @description： 工具类，服务实现层
 * @date 2019/11/14
 */
@Slf4j
@Service
public class ToolServiceImpl implements ToolService {

    private static NormalDistribution d = new NormalDistribution(0,1);

    // 默认除法运算精度
    private static final Integer DEF_DIV_SCALE = 8;

    private static final BigDecimal ONE = new BigDecimal("1");
    private static final BigDecimal TWO = new BigDecimal("2");

    @Override
    public String compareMeanAndConst(BigDecimal difference, BigDecimal std, Integer lateral, BigDecimal exam_a, BigDecimal exam_b) {
        //计算Z分布
        if (new Integer(1).equals(lateral)) {
            exam_a = exam_a.divide(TWO, DEF_DIV_SCALE, BigDecimal.ROUND_HALF_UP);
        }

        Double zA = getInverseCumulativeProbability(exam_a);
        Double zB = getInverseCumulativeProbability(exam_b);

        //计算平方
        BigDecimal sqDifferenceBigDecimal = difference.multiply(difference);
        BigDecimal sqStdBigDecimal = std.multiply(std);

        //精准运算
        BigDecimal zABigDecimal = new BigDecimal(Double.toString(zA));
        BigDecimal zBBigDecimal = new BigDecimal(Double.toString(zB));
        BigDecimal aAndBBigDecimal = zABigDecimal.add(zBBigDecimal);
        BigDecimal sqAandBBigDecimal = aAndBBigDecimal.multiply(aAndBBigDecimal);

        BigDecimal topPart = sqAandBBigDecimal.multiply(sqStdBigDecimal);

        //得到样本量
        BigDecimal nBigDecimal = null;
        try {
            nBigDecimal = topPart.divide(sqDifferenceBigDecimal, DEF_DIV_SCALE, RoundingMode.HALF_UP);
        } catch (Exception e) {
            throw new ServiceException("样本均值与固定值（总体均值）比较的差不能为0");
        }

        //向上取整
        return getIntegerFromBigDecimal(nBigDecimal);
    }

    @Override
    public String compareIndependenceMean(BigDecimal uT, BigDecimal uC, BigDecimal combineStd, Integer lateral, BigDecimal exam_a, BigDecimal exam_b, BigDecimal r) {
        //计算Z分布
        if (new Integer(1).equals(lateral)) {
            exam_a = exam_a.divide(TWO, DEF_DIV_SCALE, BigDecimal.ROUND_HALF_UP);
        }

        Double zA = getInverseCumulativeProbability(exam_a);
        Double zB = getInverseCumulativeProbability(exam_b);

        //计算Z分布的和后平方
        BigDecimal zABigDecimal = new BigDecimal(Double.toString(zA));
        BigDecimal zBBigDecimal = new BigDecimal(Double.toString(zB));
        BigDecimal aAndBBigDecimal = zABigDecimal.add(zBBigDecimal);
        BigDecimal sqAandBBigDecimal = aAndBBigDecimal.multiply(aAndBBigDecimal);

        //计算Z分布的平方与标准差平方的积
        BigDecimal sqCombineStd = combineStd.multiply(combineStd);
        BigDecimal mainTop = sqAandBBigDecimal.multiply(sqCombineStd);

        //计算实验组均值减去对照组均值做差之后的平方
        BigDecimal bottom = uT.subtract(uC);
        BigDecimal sqBottom = bottom.multiply(bottom);

        BigDecimal mainCapter = null;
        try {
            mainCapter = mainTop.divide(sqBottom, DEF_DIV_SCALE, RoundingMode.HALF_UP);
        } catch (Exception e) {
            throw new ServiceException("uT与uC之差，不能为0");
        }

        //计算r+1
        BigDecimal rTop = r.add(BigDecimal.ONE);
        //计算r+1除以r
        BigDecimal rCapter = null;
        try {
            rCapter = rTop.divide(r, DEF_DIV_SCALE, RoundingMode.HALF_UP);
        } catch (Exception e) {
            throw new ServiceException("试验组与对照组样本量之比，不能为0");
        }

        //得到样本量
        BigDecimal nBigDecimal = rCapter.multiply(mainCapter);

        return getIntegerFromBigDecimal(nBigDecimal);
    }

    @Override
    public String compareNonInferiority(BigDecimal testGroupMean, BigDecimal controlGroupMean, BigDecimal combineStdNonInferiority, BigDecimal dividingValue, BigDecimal nonInferiority_a, BigDecimal nonInferiority_b, BigDecimal nonInferiorityR) {
        //计算Z分布
        Double zA = getInverseCumulativeProbability(nonInferiority_a);
        Double zB = getInverseCumulativeProbability(nonInferiority_b);
        BigDecimal zABigDecimal = new BigDecimal(Double.toString(zA));
        BigDecimal zBBigDecimal = new BigDecimal(Double.toString(zB));
        BigDecimal aAndBBigDecimal = zABigDecimal.add(zBBigDecimal);
        BigDecimal sqAandBBigDecimal = aAndBBigDecimal.multiply(aAndBBigDecimal);

        //计算试验组与对照组差值的平方
        BigDecimal diff = testGroupMean.subtract(controlGroupMean);

        //计算总体标准差平方
        BigDecimal sqCombineStdNonInferiority = combineStdNonInferiority.multiply(combineStdNonInferiority);

        //计算总体分子
        BigDecimal mainTop = sqAandBBigDecimal.multiply(sqCombineStdNonInferiority);

        //计算试验组与对照组差值 - 非劣效界值
        BigDecimal mainBottom = diff.subtract(dividingValue);
        BigDecimal sqMainBottom = mainBottom.multiply(mainBottom);

        //计算r+1
        BigDecimal rTop = nonInferiorityR.add(BigDecimal.ONE);
        //计算分子乘积
        BigDecimal allTop = rTop.multiply(mainTop);

        //计算分母乘积
        BigDecimal allBottom = nonInferiorityR.multiply(sqMainBottom);

        BigDecimal nBigDecimal = allTop.divide(allBottom, DEF_DIV_SCALE, RoundingMode.HALF_UP);

        return getIntegerFromBigDecimal(nBigDecimal);
    }

    @Override
    public String compareSuperiorityTest(BigDecimal superiorityTestTestGroupMean,
                                         BigDecimal superiorityTestControlGroupMean,
                                         BigDecimal superiorityTestCombineStdNonInferiority,
                                         BigDecimal superiorityTestDividingValue,
                                         BigDecimal superiorityTest_a,
                                         BigDecimal superiorityTest_b,
                                         BigDecimal superiorityTestR) {
        //计算Z分布
        Double zA = getInverseCumulativeProbability(superiorityTest_a);
        Double zB = getInverseCumulativeProbability(superiorityTest_b);
        BigDecimal zABigDecimal = new BigDecimal(Double.toString(zA));
        BigDecimal zBBigDecimal = new BigDecimal(Double.toString(zB));
        BigDecimal aAndBBigDecimal = zABigDecimal.add(zBBigDecimal);
        BigDecimal sqAandBBigDecimal = aAndBBigDecimal.multiply(aAndBBigDecimal);

        //计算试验组与对照组差值的平方
        BigDecimal diff = superiorityTestTestGroupMean.subtract(superiorityTestControlGroupMean);

        //计算总体标准差平方
        BigDecimal sqCombineStdNonInferiority = superiorityTestCombineStdNonInferiority.multiply(superiorityTestCombineStdNonInferiority);

        //计算总体分子
        BigDecimal mainTop = sqAandBBigDecimal.multiply(sqCombineStdNonInferiority);

        //计算试验组与对照组差值 - 优效界值
        BigDecimal mainBottom = diff.subtract(superiorityTestDividingValue);
        BigDecimal sqMainBottom = mainBottom.multiply(mainBottom);

        //计算r+1
        BigDecimal rTop = superiorityTestR.add(BigDecimal.ONE);
        //计算分子乘积
        BigDecimal allTop = rTop.multiply(mainTop);

        //计算分母乘积
        BigDecimal allBottom = superiorityTestR.multiply(sqMainBottom);

        BigDecimal nBigDecimal = null;
        try {
            nBigDecimal = allTop.divide(allBottom, DEF_DIV_SCALE, RoundingMode.HALF_UP);
        } catch (Exception e) {
            throw new ServiceException("公式分母部分不能为0");
        }

        return getIntegerFromBigDecimal(nBigDecimal);
    }

    @Override
    public String compareRatioAndConstant(BigDecimal ratioAndConstant_testGroup_pai_1,
                                          BigDecimal ratioAndConstant_base_pai_0,
                                          Integer ratioAndConstant_lateral,
                                          BigDecimal ratioAndConstant_a,
                                          BigDecimal ratioAndConstant_b) {
        //计算Z分布
        if (new Integer(1).equals(ratioAndConstant_lateral)) {
            ratioAndConstant_a = ratioAndConstant_a.divide(TWO, DEF_DIV_SCALE, BigDecimal.ROUND_HALF_UP);
        }

        //计算Z分布
        Double zA = getInverseCumulativeProbability(ratioAndConstant_a);
        Double zB = getInverseCumulativeProbability(ratioAndConstant_b);
        BigDecimal zABigDecimal = new BigDecimal(Double.toString(zA));
        BigDecimal zBBigDecimal = new BigDecimal(Double.toString(zB));

        //计算检验水准a的top
        BigDecimal topA = zABigDecimal.multiply(getRadicalByPai(ratioAndConstant_testGroup_pai_1));
        //计算检验水准b的top
        BigDecimal topB = zBBigDecimal.multiply(getRadicalByPai(ratioAndConstant_base_pai_0));
        //计算topA+topB
        BigDecimal topAddRes = topA.add(topB);
        //计算top
        BigDecimal top = topAddRes.multiply(topAddRes);

        //计算Π1-Π0
        BigDecimal bottomSubRes = ratioAndConstant_testGroup_pai_1.subtract(ratioAndConstant_base_pai_0);
        //计算bottom
        BigDecimal bottom = bottomSubRes.multiply(bottomSubRes);

        BigDecimal nBigDecimal = null;
        try {
            nBigDecimal = top.divide(bottom, DEF_DIV_SCALE, RoundingMode.HALF_UP);
        } catch (Exception e) {
            throw new ServiceException("试验组率与基线率之差不能为0");
        }

        return getIntegerFromBigDecimal(nBigDecimal);
    }

    @Override
    public String compareIndependenceRatio(BigDecimal independenceRatio_testGroup_pi_T,
                                           BigDecimal independenceRatio_controlGroup_pi_C,
                                           Integer independenceRatio_lateral,
                                           BigDecimal independenceRatio_a,
                                           BigDecimal independenceRatio_b,
                                           BigDecimal independenceRatioR) {
        //计算Z分布
        if (new Integer(1).equals(independenceRatio_lateral)) {
            independenceRatio_a = independenceRatio_a.divide(TWO, DEF_DIV_SCALE, BigDecimal.ROUND_HALF_UP);
        }
        //计算Z分布和的平方
        Double zA = getInverseCumulativeProbability(independenceRatio_a);
        Double zB = getInverseCumulativeProbability(independenceRatio_b);
        BigDecimal zABigDecimal = new BigDecimal(Double.toString(zA));
        BigDecimal zBBigDecimal = new BigDecimal(Double.toString(zB));
        BigDecimal aAndBBigDecimal = zABigDecimal.add(zBBigDecimal);
        BigDecimal sqAandBBigDecimal = aAndBBigDecimal.multiply(aAndBBigDecimal);

        //计算 piT计算结果和piC计算结果的加合
        BigDecimal piT = ONE.subtract(independenceRatio_testGroup_pi_T).multiply(independenceRatio_testGroup_pi_T);
        BigDecimal piC = null;
        try {
            piC = ONE.subtract(independenceRatio_controlGroup_pi_C).multiply(independenceRatio_controlGroup_pi_C)
                    .divide(independenceRatioR, DEF_DIV_SCALE, RoundingMode.HALF_UP);
        } catch (Exception e) {
            throw new ServiceException("试验组与对照组样本量之比r，不能为0");
        }
        BigDecimal piTAndC = piT.add(piC);

        //计算分子
        BigDecimal top = sqAandBBigDecimal.multiply(piTAndC);

        //计算piT与piC的差的平方
        BigDecimal piTSubC = independenceRatio_testGroup_pi_T.subtract(independenceRatio_controlGroup_pi_C);
        BigDecimal sqPiSubC = piTSubC.multiply(piTSubC);

        BigDecimal nBigDecimal = null;
        try {
            nBigDecimal = top.divide(sqPiSubC, DEF_DIV_SCALE, RoundingMode.HALF_UP);
        } catch (Exception e) {
            throw new ServiceException("试验组率与对照组率之差不能为0");
        }

        return getIntegerFromBigDecimal(nBigDecimal);
    }

    @Override
    public String compareIndependenceRatioNonInferiority(BigDecimal irni_testGroup_pi_T,
                                                         BigDecimal irni_controlGroup_pi_C,
                                                         BigDecimal irniDividingValue,
                                                         BigDecimal irni_a,
                                                         BigDecimal irni_b,
                                                         BigDecimal irniR) {
        //计算Z分布
        Double zA = getInverseCumulativeProbability(irni_a);
        Double zB = getInverseCumulativeProbability(irni_b);
        BigDecimal zABigDecimal = new BigDecimal(Double.toString(zA));
        BigDecimal zBBigDecimal = new BigDecimal(Double.toString(zB));
        BigDecimal aAndBBigDecimal = zABigDecimal.add(zBBigDecimal);
        BigDecimal sqAandBBigDecimal = aAndBBigDecimal.multiply(aAndBBigDecimal);

        //计算试验组与对照组率的差
        BigDecimal piTSubPiC = irni_testGroup_pi_T.subtract(irni_controlGroup_pi_C);

        //计算piTSubPiC与非劣效界值的差的平方
        BigDecimal piTSubPiCSubDividingValue = piTSubPiC.subtract(irniDividingValue);
        BigDecimal sqPiTSubPiCSubDividingValue = piTSubPiCSubDividingValue.multiply(piTSubPiCSubDividingValue);

        //计算piC和piT的计算结果，和加合
        BigDecimal piC = ONE.subtract(irni_controlGroup_pi_C).multiply(irni_controlGroup_pi_C);
        BigDecimal piT = null;
        try {
            piT = ONE.subtract(irni_testGroup_pi_T).multiply(irni_testGroup_pi_T).divide(irniR, DEF_DIV_SCALE, RoundingMode.HALF_UP);
        } catch (Exception e) {
            throw new ServiceException("试验组与对对照组样本量之比 r，不能为0");
        }
        BigDecimal piCAndPiT = piC.add(piT);

        //计算分子
        BigDecimal top = sqAandBBigDecimal.multiply(piCAndPiT);

        BigDecimal nBigDecimal = null;
        try {
            nBigDecimal = top.divide(sqPiTSubPiCSubDividingValue, DEF_DIV_SCALE, RoundingMode.HALF_UP);
        } catch (Exception e) {
            throw new ServiceException("公式分母部分不能为0");
        }

        return getIntegerFromBigDecimal(nBigDecimal);
    }

    @Override
    public String compareIndependenceRatioSuperiorityTest(BigDecimal irst_testGroup_pi_T, BigDecimal irst_controlGroup_pi_C, BigDecimal irstDividingValue, BigDecimal irst_a, BigDecimal irst_b, BigDecimal irstR) {
        //计算Z分布
        Double zA = getInverseCumulativeProbability(irst_a);
        Double zB = getInverseCumulativeProbability(irst_b);
        BigDecimal zABigDecimal = new BigDecimal(Double.toString(zA));
        BigDecimal zBBigDecimal = new BigDecimal(Double.toString(zB));
        BigDecimal aAndBBigDecimal = zABigDecimal.add(zBBigDecimal);
        BigDecimal sqAandBBigDecimal = aAndBBigDecimal.multiply(aAndBBigDecimal);

        //计算试验组与对照组率的差
        BigDecimal piTSubPiC = irst_testGroup_pi_T.subtract(irst_controlGroup_pi_C);

        //计算piTSubPiC与非劣效界值的差的平方
        BigDecimal piTSubPiCSubDividingValue = piTSubPiC.subtract(irstDividingValue);
        BigDecimal sqPiTSubPiCSubDividingValue = piTSubPiCSubDividingValue.multiply(piTSubPiCSubDividingValue);

        //计算piC和piT的计算结果，和加合
        BigDecimal piC = ONE.subtract(irst_controlGroup_pi_C).multiply(irst_controlGroup_pi_C);
        BigDecimal piT = null;
        try {
            piT = ONE.subtract(irst_testGroup_pi_T).multiply(irst_testGroup_pi_T).divide(irstR, DEF_DIV_SCALE, RoundingMode.HALF_UP);
        } catch (Exception e) {
            throw new ServiceException("试验组与对对照组样本量之比 r，不能为0");
        }
        BigDecimal piCAndPiT = piC.add(piT);

        //计算分子
        BigDecimal top = sqAandBBigDecimal.multiply(piCAndPiT);

        BigDecimal nBigDecimal = null;
        try {
            nBigDecimal = top.divide(sqPiTSubPiCSubDividingValue, DEF_DIV_SCALE, RoundingMode.HALF_UP);
        } catch (Exception e) {
            throw new ServiceException("公式分母部分，不能为0");
        }

        return getIntegerFromBigDecimal(nBigDecimal);
    }

    @Override
    public String estimationSamplSizeByClassifiedVariable(BigDecimal ssbcv_Sensibility,
                                                          BigDecimal ssbcv_ErrorRange,
                                                          Integer ssbcv_lateral,
                                                          BigDecimal ssbcv_a) {
        //计算Z分布
        if (new Integer(1).equals(ssbcv_lateral)) {
            ssbcv_a = ssbcv_a.divide(TWO, DEF_DIV_SCALE, BigDecimal.ROUND_HALF_UP);
        }
        Double zA = getInverseCumulativeProbability(ssbcv_a);
        BigDecimal zABigDecimal = new BigDecimal(Double.toString(zA));
        BigDecimal sqZABigDecimal = zABigDecimal.multiply(zABigDecimal);

        //计算灵敏度/特异度部分
        BigDecimal pCapter = ONE.subtract(ssbcv_Sensibility).multiply(ssbcv_Sensibility);

        //计算分子
        BigDecimal top = sqZABigDecimal.multiply(pCapter);

        //计算允许误差大小的平方
        BigDecimal sqErrorRange = ssbcv_ErrorRange.multiply(ssbcv_ErrorRange);

        BigDecimal nBigDecimal = null;
        try {
            nBigDecimal = top.divide(sqErrorRange, DEF_DIV_SCALE, RoundingMode.HALF_UP);
        } catch (Exception e) {
            throw new ServiceException("灵敏度估计误差范围，不能为0");
        }

        return getIntegerFromBigDecimal(nBigDecimal);
    }

    private String getIntegerFromBigDecimal(BigDecimal n) {
        return n.setScale(0, BigDecimal.ROUND_UP).toString();
    }

    private Double getInverseCumulativeProbability(BigDecimal exam) {
        try {
            Double z = d.inverseCumulativeProbability(ONE.subtract(exam).doubleValue());
            return z;
        } catch (OutOfRangeException e) {
            throw new ServiceException("请检查检验水准填写是否正确");
        }
    }

    private BigDecimal getRadicalByPai(BigDecimal pai) {
        //计算1-Π
        BigDecimal subPai = ONE.subtract(pai);

        //计算Π乘subPai
        BigDecimal product = pai.multiply(subPai);

        return standardDeviation(product);
    }

    /**
     * math开平方
     * @param x
     * @return
     */
    public BigDecimal standardDeviation(BigDecimal x) {
        Double xDouble = x.doubleValue();

        Double sqrtX = Math.sqrt(xDouble);

        return new BigDecimal(Double.toString(sqrtX));
    }
}
