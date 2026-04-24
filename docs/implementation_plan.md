# Protótipo de Urna Eletrônica Segura (Versão 2.0 - Trust No One)

Este documento detalha o plano de implementação para a construção de um protótipo de urna eletrônica focado na proteção contra **Administradores Maliciosos (TSE)** e **Coação por Grupos Criminosos**, priorizando a soberania do eleitor e a auditabilidade pública.

## Arquitetura: Totalmente Isolada (Air-Gapped)

Atendendo ao seu feedback, eliminamos qualquer interface de rede da Urna. O sistema é projetado para ser matematicamente seguro, mesmo que o hardware seja operado em ambiente hostil.

1. **Urna Eletrônica (Hardware Isolado)**:
    - **Sem Rede**: A Urna não possui Wi-Fi, Ethernet ou Bluetooth.
    - **O Elo Físico (VVPAT - Voter Verified Paper Audit Trail)**: 
        1. Para resolver o ceticismo perceptível, a Urna possui uma impressora térmica acoplada atrás de um visor de vidro.
        2. Após a revisão digital, a Urna imprime o voto (Nome e Foto do Candidato) e o Hash de Rastreio correspondente.
        3. O eleitor confere visualmente: "O papel condiz com minha escolha". 
        4. Ao confirmar, o papel cai em uma urna física lacrada. O papel **nunca** sai da urna nem toca a mão do eleitor (preservando o sigilo e evitando venda de votos).
    - **Auditoria de Última Instância**: O Merkle Audit Ledger fornece a integridade global, mas o voto de papel é o "Voto Soberano". Em caso de auditoria por amostragem ou contestação, a contagem manual do papel deve bater com o registro digital. Qualquer divergência anula a seção.
    - **Recibo de Auditoria do Eleitor**: A Urna exibe um **Hash de Rastreio Anônimo** (derivado do Token). O eleitor pode usá-lo para consultar o portal e verificar se seu voto está no Merkle Ledger. 
    - **Segurança Contra Coação**: O portal confirmará apenas a **presença e integridade** do voto ("Voto Confirmado"), mas **nunca revelará o candidato escolhido**. Isso garante que o eleitor possa auditar seu voto sem que um coator ou comprador possa exigir a prova do candidato votado.
    - **Hardware (Simulado)**: A Urna opera com bateria interna para suportar falhas de energia e garantir que o bloco atual seja selado antes de qualquer desligamento.

2. **Centro de Processamento e Transparência Radical**:
    - **Distribuição Multi-Entidade**: Cópias idênticas do Transparency Ledger de cada urna são enviadas para o TSE e para entidades cadastradas (OAB, Universidades, Partidos). 
    - **Imutabilidade Distribuída**: Como as entidades possuem cópias, o TSE não pode alterar o Ledger central sem ser desmascarado pelo Hash do Boletim de Urna impresso e pelas cópias em posse da sociedade civil.
    - **Totalizador (Java)**: Consolida os votos. Qualquer cidadão pode usar o Hash impresso na seção para conferir se o log da sua urna foi processado corretamente no portal.

## User Review Required

> [!IMPORTANT]
> **Foco no TSE Malicioso (Remoção do Banco Decoy):**
> Conforme sugerido, eliminamos o mecanismo de "Banco Decoy" para simplificar a arquitetura e remover o risco de o TSE trocar os bancos de dados. A segurança agora reside 100% na **Auditabilidade Pública do Merkle Ledger**. Se o TSE tentar inserir votos falsos ou remover votos legítimos, a matemática do Merkle Ledger (assinada com chaves que o TSE não possui) provará a fraude instantaneamente para qualquer auditor independente.

> [!CAUTION]
> **O Token de Papel (2FA Independente):**
> O eleitor gera seu par de chaves (Pública/Privada) fora da infraestrutura do TSE (ex: em casa ou em postos de universidades/OAB). 
> 1. Ele gera um **Token de Autorização** (hash assinado) e o anota ou imprime em papel.
> 2. Na Urna, esse código libera o voto.
> 3. **Vantagem:** O TSE nunca viu a chave privada, logo, não pode "votar por ninguém" ou criar votos fantasmas.

## Análise de Riscos e Impeditivos

### 1. Possibilidade de Força Bruta no Token
- **Mitigação:** 
    - **Bloqueio e Desbloqueio:** A Urna trava após 5 tentativas. O eleitor pode optar por anular o voto ou chamar o mesário para um desbloqueio manual (requer nova autenticação).
    - **Entrada Dupla:** Suporte a **QR Code** (rápido e seguro) e **Digitação Manual** (para quem não confia em câmeras ou tem o código impresso/anotado).

### 2. Impeditivo de Usabilidade (Inclusão Digital)
- **Solução:** O modelo de "Confiança Descentralizada" permite que o eleitor recorra a postos oficiais (OAB, Universidades) ou a **familiares e amigos de confiança** para gerar seu par de chaves e token, sem precisar depender de sistemas do governo.

### 3. Prevenção contra Urnas e Eleitores Fictícios (Ghost Voting)
- **Risco:** O TSE criar urnas falsas ou gerar chaves de eleitores fantasmas.
- **Mitigação:** 
    - **Registro de Urnas Autorizadas:** Antes da eleição, uma lista com o ID e a Chave Pública de cada Urna física é assinada por um **Comitê de Auditoria Independente** (OAB, Universidades, Partidos). Blocos vindos de urnas fora dessa lista são rejeitados pelo totalizador e pelos scripts de auditoria dos cidadãos.
    - **Root of Trust Descentralizada e Resiliência**: 
        1. O Registro Público de Chaves é um **Transparency Ledger**. Além do TSE e entidades, **qualquer cidadão pode baixar uma cópia completa** desse registro. Isso permite que cada eleitor verifique se sua chave pública está correta e se não houve inserção de chaves falsas.
        2. **Privacidade e Identificadores**: No registro público, o identificador será um **Hash Seguro do Título de Eleitor** (SHA-256). Assim, o cidadão usa seu número de Título para gerar o hash (no app ou via script) e encontrar sua chave pública na lista, mas um terceiro não consegue "varrer" a lista para descobrir quem é quem sem ter os números dos títulos.
        3. **Auditoria Cidadã e Open Source**: Os scripts de auditoria serão **100% Open Source**. 
        4. **Prova de Fraude**: Se o TSE publicar um resultado diferente do que os scripts independentes calcularem, o **Merkle Ledger Público serve como prova irrefutável da fraude**. Como os dados são públicos, qualquer tribunal (ou a própria sociedade) pode reprocessar a soma e expor a mentira do administrador.
        5. **Auditoria Manual e por Amostragem**: Embora validar milhões de hashes manualmente seja impossível para um humano, o cidadão pode fazer uma **Auditoria de Amostragem**: escolher uma Urna específica, baixar seus votos e conferir se a contagem para o Candidato X bate exatamente com o valor impresso no **Boletim de Urna**. Como cada voto é assinado individualmente pelo eleitor, o TSE não pode alterar o total do Boletim sem forjar assinaturas digitais que ele não possui.

## Argumentos contra o Ceticismo Extremo

Para rebater as dúvidas de quem não confia em sistemas digitais, este modelo oferece respostas baseadas na **soberania da matemática** sobre a **confiança em instituições**:

1.  **"O software pode ser alterado para trocar o voto":**
    *   *Resposta:* O software não tem poder para alterar seu voto porque ele não possui sua **Chave Privada**. Se o software tentasse mudar "Candidato A" para "B", a assinatura digital que você gerou com seu Token seria invalidada. A fraude seria detectada instantaneamente por qualquer script de auditoria. O poder de selar o voto é seu, não da máquina.
2.  **"O TSE pode apagar votos da base de dados":**
    *   *Resposta:* Os dados estão em um **Distributed Append-only Ledger**. Para apagar um voto, o TSE teria que apagar simultaneamente as cópias em posse da OAB, Universidades, Partidos e dos próprios cidadãos. É impossível deletar a história quando ela está em milhares de mãos ao mesmo tempo.
3.  **"Eu quero ver meu voto no papel":**
    *   *Resposta:* O seu **Hash de Rastreio** e o seu **Token** são sua "cédula digital". Diferente do papel, que pode ser extraviado ou queimado no transporte, o seu rastreio está "escrito na pedra" do Ledger público. Você pode conferir se ele foi contado a qualquer hora, de qualquer lugar, sem depender de ninguém.
4.  **"Só especialistas entendem isso":**
    *   *Resposta:* Você não precisa entender a matemática, basta observar o **Consenso**. Se o partido da situação, o partido da oposição e a universidade independente rodarem a auditoria e todos chegarem ao mesmo número, a verdade é absoluta. O sistema é desenhado para que até inimigos políticos concordem com o resultado final.

## Proposed Changes

### `backend-java/` (Core Criptográfico e Auditoria)
- **Merkle Engine**: Implementação de Merkle Trees para garantir que nenhum voto seja alterado sem quebrar toda a corrente.
- **Signature Validator**: Módulo que valida o Token do eleitor contra o Registro Público de Chaves (Open PKI).

### `urna-ui/` (Simulador de Hardware)
- Interface simulando a ausência de conexão.
- Campo para inserção do Token alfanumérico/numérico de papel.

### `audit-tool/` (Script de Auditoria Independente)
- Um pequeno utilitário (CLI ou Web) para que o cidadão valide o Audit Ledger baixado em sua própria máquina.

### Cenários a Validar (Casos de Uso da PoC)
1. **Teste de Integridade**: Tentar alterar um voto no arquivo do Ledger e verificar se o Totalizador Java detecta a quebra do Hash.
2. **Teste de Autenticidade**: Tentar votar com um Token gerado pelo "TSE Malicioso" (sem chave privada válida no Registro Público) e garantir que a Urna recuse.
3. **Simulação Air-Gap**: Garantir que o software da Urna funcione 100% sem qualquer chamada de API externa durante o processo de votação.
