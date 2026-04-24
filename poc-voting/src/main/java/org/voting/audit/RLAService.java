package org.voting.audit;

public class RLAService {
    /**
     * Calcula o tamanho da amostra necessária para uma Auditoria de Risco Limitado (RLA).
     * @param margin Margem de vitória (ex: 0.02 para 2%)
     * @param alpha Nível de risco (ex: 0.01 para 99% de confiança)
     * @return Número de urnas a serem auditadas manualmente.
     */
    public int calculateSampleSize(double margin, double alpha) {
        if (margin <= 0) return 0;
        // Fórmula simplificada de Bravo: n = -ln(alpha) / (2 * margin^2)
        return (int) Math.ceil(-Math.log(alpha) / (2 * Math.pow(margin, 2)));
    }
}
