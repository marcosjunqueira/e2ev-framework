# Documentação de Arquitetura: Sistema de Transparência Eleitoral (v6.0)

## 1. Visão Geral
O sistema é um framework de **Votação Verificável de Ponta a Ponta (E2E-V)** baseado no princípio de **Strong Software Independence (SSI)**. A arquitetura hibridiza a imutabilidade criptográfica com a soberania do registro físico (papel).

## 2. Componentes do Sistema

### 2.1. Urna Eletrônica (Terminal de Votação)
- **Função**: Coletar o voto, imprimir o VVPAT e registrar o voto no Log de Transparência.
- **Hardware**: Isolado (Air-gapped), com impressora térmica protegida por vidro e armazenamento redundante (Journaling).
- **Invariante**: Não possui acesso à chave privada do eleitor nem à base de dados de identidades.

### 2.2. Autoridade de Autenticação (Blind Signer)
- **Função**: Validar a elegibilidade do eleitor e emitir Tokens de Voto.
- **Tecnologia**: RSA Blind Signatures.
- **Propriedade**: Garante que o TSE saiba **quem** votou, mas não **em qual token** o voto foi registrado.

### 2.3. Log de Transparência (Merkle Audit Ledger)
- **Função**: Registro público e imutável de todos os eventos da eleição.
- **Estrutura**: Merkle-Chaining (Encadeamento de hashes SHA-256).
- **Distribuição**: Replicado em tempo real entre entidades independentes (OAB, Universidades, Partidos).

### 2.4. Portal de Auditoria Cidadã
- **Função**: Interface pública para conferência de hashes e totalização paralela.
- **Recursos**: Download da Blockchain, scripts de auditoria Open Source e verificação de recibos deniáveis.

## 3. O Fluxo do Voto (Life Cycle)

1.  **Geração de Chave (Eleitor)**: O eleitor gera seu par de chaves (Open PKI) em ambiente seguro.
2.  **Autorização (2FA)**: O eleitor se autentica; o TSE assina cegamente seu token de voto.
3.  **Votação (Cabine)**: 
    - O eleitor apresenta o Token assinado.
    - A Urna valida a assinatura e colhe o voto.
    - A Urna imprime o **VVPAT** (conferência visual).
4.  **Registro (Log)**: O voto é selado no Log de Transparência da Urna.
5.  **Totalização e Shuffle**: Os logs de todas as urnas passam por uma **Mixnet** para quebrar correlação temporal e são somados publicamente.
6.  **Auditoria (RLA)**: Uma amostra estatística de urnas é aberta para contagem manual do papel e confronto com o log digital.

## 4. Camadas de Segurança (Defesa em Profundidade)

- **Camada 1 (Criptográfica)**: Blind Signatures e Merkle Logs detectam qualquer alteração digital ou inserção de votos falsos.
- **Camada 2 (Física)**: O VVPAT garante que o eleitor valide o que a máquina registrou. O papel é a fonte soberana da verdade.
- **Camada 3 (Estatística)**: RLAs garantem que fraudes sistêmicas sejam detectadas com 99% de confiança via amostragem reduzida.
- **Camada 4 (Social)**: O consenso entre entidades rivais (que possuem cópias do log) impede a manipulação centralizada dos resultados.

## 5. Protocolo de Recuperação e Falha

| Evento | Ação de Recuperação |
| :--- | :--- |
| **Falha de Hardware** | Substituição da Urna; leitura do Log Digital + Journaling para continuidade. |
| **Divergência Digital/Física** | A contagem de papel anula o registro digital daquela seção. O Log Digital é usado para perícia forense. |
| **Perda de Dados no TSE** | Restauração imediata a partir dos nós das entidades independentes (OAB/Univ). |

## 6. Conclusão
A arquitetura v6.0 elimina o "Ponto Único de Falha" e o "Ponto Único de Confiança". A verdade emerge do consenso entre a matemática, o papel e a fiscalização humana distribuída.
