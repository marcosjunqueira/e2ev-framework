# Especificação do Protocolo de Transparência Eleitoral (v6.0 - Arquitetura Final)

Este documento define um framework experimental de votação baseado em **Strong Software Independence (SSI)** e **Transparência Pública Distribuída**.

## 1. Modelo de Confiança (Trusted Assumptions)

Nenhum sistema elimina a confiança. Este protocolo as explicita:

| Premissa (Assumption) | Impacto | Necessidade |
| :--- | :--- | :--- |
| **Integridade do VVPAT** | O papel não é substituído fisicamente na urna. | Crítica (Soberania) |
| **RLA Independente** | A auditoria estatística é feita por múltiplos atores rivais. | Crítica (Legitimidade) |
| **Mixnets Distribuídas** | Pelo menos um nó da mixnet é honesto. | Necessária (Anonimato) |
| **Hardware Attestation** | O Secure Boot garante o firmware básico. | Desejável (Resiliência) |
| **Entropia de Nonce** | O gerador de números aleatórios é seguro. | Necessária (Criptografia) |

## 2. Strong Software Independence (SSI)

O protocolo adere à definição de **Strong SI**: O sistema não apenas detecta erros ou fraudes no software, mas permite a **reconstrução do resultado correto** a partir da fonte soberana (VVPAT) sem a necessidade de repetir o pleito.

## 3. Laboratório de Invariantes

O sistema é validado pela prova de cinco invariantes fundamentais:

1. **Blind Unlinkability**: Impossibilidade de ligar Identidade ↔ Voto.
2. **Append-only Transparency**: Qualquer remoção ou alteração de voto deixa rastro criptográfico.
3. **Paper Sovereignty**: O papel é a verdade jurídica; o digital é a evidência forense.
4. **RLA Detectability**: Fraude sistêmica é detectável com confiança estatística mínima (99%).
5. **Receipt-Freeness (Deniable Verification)**: O eleitor pode verificar seu voto, mas não pode provar seu conteúdo para um coator (uso de canais de negação).

## 4. Statistical Assurance Model (RLA)

A legitimidade estatística é definida por:
- **Risco Limite (Alpha)**: 1% (Confiança de 99%).
- **Margem de Erro (Epsilon)**: Baseada na diferença real entre candidatos.
- **Protocolo de Escalonamento**: Se a divergência na amostra for maior que o limite de erro técnico (0.01%), a amostra é expandida automaticamente até a contagem total, se necessário.

## 5. Fluxo Criptográfico: O Ciclo de Vida do Voto

### Fase 1: Autorização (Blind Signature)
- Uso de RSA Blind Signatures para emitir tokens de elegibilidade anônimos.
### Fase 2: Mixnets (Shuffle)
- Antes da publicação, os votos passam por um processo de embaralhamento distribuído para quebrar correlação temporal.
### Fase 3: Verificação Individual Deniável
- O eleitor recebe um recibo que confirma a inclusão, mas que não permite provar a escolha feita, protegendo contra coerção.

## 6. Referências e Convergência Acadêmica
Este modelo converge deliberadamente com os princípios de:
- **STAR-Vote** (Papel + Cripto).
- **ElectionGuard** (Verificabilidade E2E).
- **Risk-Limiting Audits** (Stark/Rivest).
