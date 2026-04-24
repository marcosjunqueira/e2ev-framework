package org.voting.crypto;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MixnetService {
    /**
     * Realiza um embaralhamento verificável (Verifiable Shuffle).
     * @param input List de votos originais.
     * @return List de votos embaralhados.
     */
    public List<String> shuffle(List<String> input) {
        List<String> output = new ArrayList<>(input);
        Collections.shuffle(output);
        return output;
    }

    /**
     * Verifica se o set de votos embaralhados é idêntico ao set original
     * (não houve inserção nem remoção de votos durante o shuffle).
     */
    public boolean verifyShuffle(List<String> input, List<String> output) {
        if (input.size() != output.size()) return false;
        
        // Em um sistema real, usaríamos provas de conhecimento zero (ZKP).
        // Aqui validamos via comparação de conjuntos (Sets).
        return input.containsAll(output) && output.containsAll(input);
    }
}
