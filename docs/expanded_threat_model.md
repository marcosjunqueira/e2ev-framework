# Modelo de Ameaças Expandido (Expanded Threat Model)

Este documento detalha os vetores de ataque mais complexos e as dependências institucionais do protocolo.

## 1. Ataques à Cadeia de Suprimentos (Supply Chain) e Hardware
**Ameaça**: Compromisso de componentes físicos (Firmware USB, BIOS maliciosa, microcódigo adulterado, impressora com memória persistente oculta, backdoors em nível de silício).
**Mitigação SSI**: Qualquer alteração maliciosa que mude o comportamento de registro da máquina (e.g., registrando A no log digital e imprimindo B no VVPAT) será detectada pelo eleitor no momento da auditoria visual, e confirmada através da RLA e divergência do Merkle Log.
**Risco Residual**: Ataques de Firmware que afetam o Gerador de Números Aleatórios (RNG), tornando os nonces previsíveis e enfraquecendo a criptografia subjacente, ou ataques que simulam corretamente o VVPAT mas não gravam no log, forçando uma aparente anomalia operacional.
**Defesa Necessária**: Hardware Attestation (Secure Boot e Trusted Platform Module), builds determinísticos e open-source hardware standards (e.g., arquitetura RISC-V auditável).

## 2. Economia da Coerção (Coercion Economics)
**Ameaça**: Compra massiva de votos ou coerção sistêmica.
**Mitigação**: O sistema emprega Receipt-Freeness através de IDs negáveis (Deniable Verification). Um eleitor pode provar que votou sem provar em quem votou.
**Risco Residual**: Ameaças físicas diretas na cabine (coator fisicamente presente ou forçando a filmagem/transmissão em tempo real do ato de votar usando dispositivos ocultos).
**Defesa Necessária**: Regras rígidas e arquitetura física da seção de votação que impeçam captura de imagem sem o consentimento/conhecimento dos fiscais (fiscalização ambiental não criptográfica). O custo para fraudar escala linearmente, tornando impossível escalar a fraude para o nível estadual ou federal sem exposição.

## 3. Entropia Maliciosa (Malicious Entropy)
**Ameaça**: O RNG da urna eletrônica foi comprometido ou é enviesado, gerando nonces/chaves previsíveis ou que formam canais ocultos (Subliminal Channels).
**Mitigação/Risco Residual**: Se o RNG for comprometido, o anonimato proporcionado pela assinatura cega pode ser parcialmente quebrado via análise estatística, permitindo rastrear o token gerado.
**Defesa Necessária**: Entropia multi-fonte. Combinar a entropia gerada pelo usuário (ex: interação no terminal, movimento do mouse, input de dados aleatórios pelo eleitor na geração do token) com geradores quânticos/físicos isolados, atenuando a dependência do hardware central.

## 4. Colusão de Insiders (Insider Collusion)
**Ameaça**: O TSE, a autoridade de contagem e os auditores independentes conspiram em bloco para adulterar o pleito.
**Mitigação**: A natureza distribuída do Merkle Log. Qualquer grupo cívico pode baixar o script de auditoria open-source e o banco de dados criptográfico para computar o resultado independentemente. 
**Risco Residual**: A fraude física total. Se os fraudadores também controlarem as urnas de lona (VVPAT) e coordenarem a adulteração do papel em proporção idêntica à fraude digital.
**Defesa Necessária**: O modelo depende de *observadores independentes na cadeia de custódia física*. O sistema é Seguro se, e somente se, pelo menos uma via de fiscalização (física ou digital) permanecer honesta. Se todos os atores do processo conspirarem ativamente, nenhuma arquitetura tecnológica pode substituir a entropia democrática.
