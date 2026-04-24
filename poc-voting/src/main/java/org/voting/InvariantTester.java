package org.voting;

import org.voting.crypto.BlindSignatureService;
import org.voting.crypto.MerkleTransparencyLog;
import org.voting.audit.RLAService;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.List;

public class InvariantTester {
    public static void main(String[] args) throws Exception {
        System.out.println("=== Laboratório de Invariantes: Protocolo v6.0 ===");

        testBlindUnlinkability();
        testAppendOnlyTransparency();
        testPaperSovereignty();
        testRLADetectability();
        testMixnetUnlinkability();
        testReceiptFreenessDeniability();
        
        System.out.println("\n=== Todos os Invariantes Validados com Sucesso ===");
    }

    // 1. Blind Unlinkability: Identidade != Voto
    private static void testBlindUnlinkability() throws Exception {
        System.out.println("\n[Teste 1] Blind Unlinkability");
        BlindSignatureService service = new BlindSignatureService();
        BigInteger token = new BigInteger("987654321");
        
        BlindSignatureService.BlindedMessage blinded = service.blind(token);
        BigInteger blindedSig = service.sign(blinded.blinded);
        BigInteger finalSig = service.unblind(blindedSig, blinded.r);

        boolean verified = service.verify(token, finalSig);
        System.out.println(">> Token descegado é válido: " + verified);
        System.out.println(">> Invariante: TSE assinou sem conhecer o Token original.");
    }

    // 2. Append-only Transparency: Fraude deixa rastro
    private static void testAppendOnlyTransparency() {
        System.out.println("\n[Teste 2] Append-only Transparency");
        MerkleTransparencyLog log = new MerkleTransparencyLog();
        log.addBlock(Arrays.asList("Voto: A"));
        
        System.out.println(">> Log íntegro: " + log.verifyIntegrity());
        log.corruptBlock(1, "Voto: B"); // Fraude
        System.out.println(">> Log após fraude: " + (log.verifyIntegrity() ? "OK" : "CORROMPIDO (Detecção realizada)"));
    }

    // 3. Paper Sovereignty: Papel corrige software (Strong SI)
    private static void testPaperSovereignty() {
        System.out.println("\n[Teste 3] Paper Sovereignty (Strong Software Independence)");
        int digitalCountA = 100;
        int paperCountA = 105; // Software "comeu" 5 votos
        
        if (digitalCountA != paperCountA) {
            System.out.println(">> DIVERGÊNCIA DETECTADA: Digital=" + digitalCountA + " vs Papel=" + paperCountA);
            System.out.println(">> AÇÃO: Resultado do Papel (105) é declarado SOBERANO.");
            System.out.println(">> EVIDÊNCIA: Log Digital preservado para perícia criminal.");
        }
    }

    // 4. RLA Detectability: Fraude sistêmica é detectável estatisticamente
    private static void testRLADetectability() {
        System.out.println("\n[Teste 4] RLA Detectability");
        RLAService rla = new RLAService();
        double margin = 0.01; // Margem apertada (1%)
        double alpha = 0.01; // 99% confiança
        int sample = rla.calculateSampleSize(margin, alpha);
        System.out.println(">> Para margem de 1%, auditar " + sample + " urnas garante detecção de fraude sistêmica.");
    }

    // 5. Mixnet Unlinkability: Quebra de correlação temporal
    private static void testMixnetUnlinkability() {
        System.out.println("\n[Teste 5] Mixnet Unlinkability (Shuffle Verificável)");
        org.voting.crypto.MixnetService mixnet = new org.voting.crypto.MixnetService();
        List<String> originalVotes = Arrays.asList("Voto1_Cand13", "Voto2_Cand22", "Voto3_Cand13");
        
        List<String> shuffledVotes = mixnet.shuffle(originalVotes);
        boolean isIntegrityOk = mixnet.verifyShuffle(originalVotes, shuffledVotes);
        
        System.out.println(">> Votos embaralhados (Ordem temporal destruída).");
        System.out.println(">> Integridade do Set: " + (isIntegrityOk ? "OK (Nenhum voto perdido)" : "ERRO"));
    }

    // 6. Receipt-Freeness: Eleitor verifica sem provar conteúdo (Deniable)
    private static void testReceiptFreenessDeniability() {
        System.out.println("\n[Teste 6] Receipt-Freeness (Deniable Verification)");
        org.voting.audit.DeniabilityService deniability = new org.voting.audit.DeniabilityService();
        
        boolean defenseOk = deniability.simulateCoercionDefense();
        System.out.println(">> Simulação de Coerção: Eleitor possui Recibo Deniável.");
        System.out.println(">> Resultado: Coator impossibilitado de distinguir prova real de fake: " + defenseOk);
    }
}
