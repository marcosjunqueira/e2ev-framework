# Especificação Formal: Risk-Limiting Audits (RLA)

A garantia estatística do sistema eleitoral depende de uma RLA rigorosa e formal. Este documento define os limites de risco, o tamanho da amostragem e a mecânica de escalonamento.

## 1. Definições Técnicas
*   **Margem Declarada ($M$)**: A diferença de votos entre o vencedor declarado e o segundo colocado, expressa como uma fração do total de votos.
*   **Limite de Risco ($\alpha$)**: A probabilidade máxima de certificar um resultado eleitoral incorreto sem correção. A especificação v6.0 fixa $\alpha \le 0.01$ (99% de nível de confiança mínima).
*   **Hipótese Nula ($H_0$)**: O resultado eleitoral relatado pelo software está incorreto (o candidato vencedor não venceu). O objetivo da RLA é rejeitar $H_0$. Se não for possível rejeitá-la, a auditoria é escalonada.

## 2. Metodologia de Amostragem
Utilizaremos a metodologia **Ballot-Polling Audit** ou **Comparison Audit** (dependendo da granularidade suportada pela comissão eleitoral). O tamanho inicial da amostra aleatória ($n$) de VVPATs (votos impressos) é calculado usando a fórmula adaptada de BRAVO, considerando $\alpha$ e a margem de erro técnica tolerável ($Epsilon$).
$$n \approx \frac{-\ln(\alpha)}{2 \times M^2}$$
Para margens muito estreitas (ex: $M \le 0.005$), a amostra exigida aproxima-se rapidamente de 100%, o que leva, na prática, a uma recontagem total manual, demonstrando a robustez orgânica da matemática.

## 3. Dinâmica de Escalonamento (Escalation Protocol)

A auditoria não é um teste binário e imediato, mas um processo adaptativo em fases:
1.  **Amostragem Base**: O lote inicial de $n$ cédulas/urnas aleatórias é recuperado fisicamente.
2.  **Comparação Soberana**: Contabiliza-se manualmente o VVPAT amostrado e o resultado é confrontado com os totais digitais daquela amostra (usando a assinatura do Merkle Log correspondente à seção amostrada).
3.  **Avaliação da Diferença**:
    *   Se a divergência for $0$ ou menor que a tolerância predita: **Auditoria Encerrada**. O resultado digital é Certificado.
    *   Se a divergência for maior que a tolerância, mas ainda não capaz de alterar o vencedor: **Expansão da Amostra** (Dobra-se o número de $n$).
    *   Se a divergência persistir de forma anômala e sistemática: **Escalonamento Total**. Dispara-se uma Contagem Manual Total (100% dos VVPATs). O Log Digital é descartado para fins legais de certificação e acionado para perícia criminal.

## 4. Cenários de Empate (Tie Scenarios) e Conflito Inconciliável
Se um ataque físico adulterar as próprias urnas de VVPAT, e a RLA detectar que o VVPAT está corrompido (ex: papel violado, marcas criptográficas inválidas no QRCode), um Tribunal Técnico independente deve ser convocado, conforme os protocolos definidos na camada de Governança Institucional.
