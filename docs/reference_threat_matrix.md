# Reference Threat Matrix (Matriz de Ameaças de Referência)

Esta matriz resume a resposta do framework às principais ameaças eleitorais, modeladas sob diferentes perfis de adversários formais.

## Atores Adversários Formais
*   $\mathcal{A}_{insider}$: Administrador de TI, Mesário, Funcionário do TSE. Possui acesso lógico privilegiado e acesso físico restrito.
*   $\mathcal{A}_{coercer}$: Chefe local, milícia, empregador. Possui controle físico ou econômico sobre o eleitor, mas não possui acesso ao sistema interno.
*   $\mathcal{A}_{hardware}$: Fabricante da urna, fornecedor de chips, inteligência estatal. Possui capacidade de injetar backdoors no nível de silício ou firmware.
*   $\mathcal{A}_{network}$: Atacante externo, hacker interceptador. Possui controle sobre o meio de transmissão (Internet/Intranet).

## Matriz de Resolução

| Vetor de Ataque | Adversário Formal | Detectável? | Recuperável? | Fonte Soberana | Mitigação Principal |
| :--- | :--- | :--- | :--- | :--- | :--- |
| **Malware Alterando Voto** | $\mathcal{A}_{insider}$ / $\mathcal{A}_{hardware}$ | Sim (Sempre) | Sim (Total) | VVPAT (Papel) | Strong Software Independence, RLA |
| **Entropia/RNG Fraco** | $\mathcal{A}_{hardware}$ | Parcial | Parcial | Mixnet/Log | Entropia multi-fonte, Mixnet Shuffle |
| **Coação Física / Compra** | $\mathcal{A}_{coercer}$ | Socialmente | Parcial | Deniable Receipt | Receipt-Freeness, Fiscalização Física |
| **Conluio Institucional** | $\mathcal{A}_{insider}$ + $\mathcal{A}_{hardware}$ | Parcial | Parcial | Distribuição Cívica | Merkle Log distribuído, Código Aberto |
| **Supressão de Voto (DoS)** | $\mathcal{A}_{network}$ / $\mathcal{A}_{insider}$ | Sim | Sim | Cédula Contingência | Log offline, Protocolo de Empate Legal |
| **Injeção de Voto Falso** | $\mathcal{A}_{insider}$ / $\mathcal{A}_{network}$ | Sim (Sempre) | Sim | Assinatura Cega | Validação Criptográfica (RSA/EC) na Urna |
| **Violação da Urna de Lona** | $\mathcal{A}_{insider}$ | Sim | Não (Repetição) | Log Digital (Forense) | Cadeia de Custódia Física Multi-partidária |

## Notas sobre "Recuperabilidade"
"Recuperável" refere-se à capacidade do protocolo de restaurar a verdade eleitoral original **sem a necessidade de anular a eleição e convocar nova votação**. Em casos de fraude digital pura (Malware), a recuperação é total via VVPAT. Em casos de destruição física catastrófica simultânea (urna incendiada + log digital apagado localmente antes do sync), a recuperação depende de contingências logísticas externas ao protocolo criptográfico.
