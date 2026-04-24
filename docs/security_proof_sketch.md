# Security Proof Sketch (Esboço de Provas de Segurança)

Este documento descreve informalmente os argumentos de segurança para as propriedades fundamentais do Sistema de Transparência Eleitoral (v6.0).

## 1. Unlinkability (Incapacidade de Associação)
**Propriedade**: Um adversário com acesso total ao Log de Transparência e ao banco de dados do TSE não consegue associar um eleitor $E_i$ a um voto $V_j$.
**Argumento**: 
O protocolo utiliza Assinaturas Cegas (RSA Blind Signatures). O eleitor $E_i$ submete um nonce cego $R'$ ao TSE. O TSE valida a identidade de $E_i$ e assina $R'$, resultando em $S'$. O eleitor "descega" $S'$ obtendo a assinatura válida $S$ para o nonce original $R$. 
Quando o eleitor vota, ele apresenta $(R, S, V_j)$. A urna valida a assinatura $S$ com a chave pública do TSE.
Como a operação de cegueira envolve um fator aleatório gerado pelo eleitor, o TSE não consegue mapear o par $(R', S')$ da fase de autorização para o par $(R, S)$ apresentado na urna. A quebra dessa propriedade implicaria na quebra da segurança matemática das Assinaturas Cegas de Chaum.
A correlação temporal na urna física é mitigada pelo uso de Mixnets, que embaralham os tuplos $(R, S, V_j)$ antes de registrá-los no bloco definitivo.

## 2. Receipt-Freeness (Impossibilidade de Recibo e Deniabilidade)
**Propriedade**: O eleitor não consegue provar a um coator externo em qual candidato ele votou, mesmo se ele quiser colaborar com o coator.
**Argumento**:
O VVPAT impresso cai diretamente na urna de lona lacrada; o eleitor não o leva consigo.
O recibo digital (ID de Auditoria) entregue ao eleitor é derivado do nonce $R$ de forma a provar a *inclusão* do voto no log, mas não revela o *conteúdo* $V_j$. 
Para mitigar o caso em que o coator exige que o eleitor vote no candidato A e traga o ID de Auditoria correspondente àquele exato momento temporal, a urna pode gerar recibos falsos (Fake Receipts) e canais de ambiguidade sob comando do eleitor. Isso garante "Negação Plausível" (Plausible Deniability): qualquer evidência digital levada pelo eleitor fora da zona eleitoral não tem valor probatório matemático de qual foi o voto contido, apenas da sua participação.

## 3. Append-only Transparency (Transparência Somente-Leitura)
**Propriedade**: É impossível alterar ou remover um voto histórico sem invalidar o estado atual do Log.
**Argumento**:
O Log utiliza Merkle-Chaining. Cada bloco $B_k$ contém $Hash(B_{k-1} || Votos_k)$. Qualquer alteração em um voto dentro de $B_{k-n}$ alterará o hash de $B_{k-n}$, o que invalidará a verificação do hash em $B_{k-n+1}$, quebrando a corrente até o bloco atual. A detecção dessa falha é garantida em tempo polinomial.

## 4. Eligibility & Non-Reusability (Elegibilidade e Unicidade)
**Propriedade**: Apenas eleitores autorizados podem votar, e cada eleitor pode votar no máximo uma vez.
**Argumento**:
**Elegibilidade**: A urna só aceita pares $(R, S)$ onde $S$ é uma assinatura válida do TSE sobre $R$. Como assumimos que o esquema de assinatura (ex: RSA 2048) é seguro contra falsificação (unforgeability), é computacionalmente inviável para um eleitor gerar um par válido sem a cooperação da Autoridade de Autenticação.
**Unicidade**: O TSE emite no máximo uma assinatura cega por identidade verificada no sistema. Na fase de votação, a urna e o Log distribuído mantêm um registro dos nonces $R$ já utilizados (Nullifiers). Se um eleitor tentar submeter o mesmo par $(R, S)$ duas vezes (Double-Spending), a urna rejeitará a segunda tentativa por colisão de nonce. A emissão de novos tokens exige nova autenticação sob a mesma identidade, o que é bloqueado pelo estado da Fase 1.
