package org.voting;

import org.voting.crypto.BlindSignatureService;
import org.voting.crypto.MerkleTransparencyLog;
import org.voting.audit.RLAService;

import java.math.BigInteger;
import java.util.Arrays;

public class Main {
    public static void main(String[] args) {
        try {
            System.out.println("=== PoC: Sistema de Transparência Eleitoral v5.0 (JAVA 25) ===");
            
            BlindSignatureService tseService = new BlindSignatureService();
            MerkleTransparencyLog transparencyLog = new MerkleTransparencyLog();
            RLAService rlaService = new RLAService();

            // --- CENÁRIO 1: Blind Signature ---
            System.out.println("\n[Cenário 1] Fluxo de Votação (Duplo-Cego)");
            BigInteger myVoteToken = new BigInteger("1234567890987654321");
            BlindSignatureService.BlindedMessage blinded = tseService.blind(myVoteToken);
            BigInteger blindedSignature = tseService.sign(blinded.blinded);
            BigInteger finalSignature = tseService.unblind(blindedSignature, blinded.r);
            boolean isValid = tseService.verify(myVoteToken, finalSignature);
            System.out.println(">> Verificação do Token na Urna: " + (isValid ? "VÁLIDA" : "INVÁLIDA"));

            // --- CENÁRIO 2: Log de Transparência ---
            System.out.println("\n[Cenário 2] Log de Transparência (Integridade)");
            transparencyLog.addBlock(Arrays.asList("Voto: Cand_13", "Voto: Cand_22"));
            System.out.println(">> Integridade Inicial: " + (transparencyLog.verifyIntegrity() ? "OK" : "ERRO"));

            // --- CENÁRIO 3: Software Independence ---
            System.out.println("\n[Cenário 3] Detecção de Fraude (Software Independence)");
            transparencyLog.corruptBlock(1, "Voto: Cand_22, Voto: Cand_22");
            boolean integrity = transparencyLog.verifyIntegrity();
            System.out.println(">> Integridade após Ataque: " + (integrity ? "OK" : "FALHA DETECTADA!"));
            if (!integrity) {
                System.out.println(">> ALERTA: Fraude Digital Detectada. Auditoria de Papel Necessária.");
            }

            // --- CENÁRIO 4: RLA ---
            System.out.println("\n[Cenário 4] Auditoria Estatística (RLA)");
            double margin = 0.02; // 2%
            double alpha = 0.01; // 99% confiança
            int sample = rlaService.calculateSampleSize(margin, alpha);
            System.out.println(">> Margem: " + (margin * 100) + "% | Confiança: 99%");
            System.out.println(">> Amostra de Urnas para RLA: " + sample);

            System.out.println("\n=== Fim da PoC ===");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
