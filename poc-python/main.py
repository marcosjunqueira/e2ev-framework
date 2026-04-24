import random
import math
from blind_signature import BlindSignatureAuthority, Voter, verify_signature
from merkle_log import TransparencyLog

def run_simulation():
    print("=== PoC: Sistema de Transparência Eleitoral v5.0 (Python) ===")
    
    # 1. Setup
    authority = BlindSignatureAuthority()
    log = TransparencyLog()
    
    # --- CENÁRIO 1: Blind Signature (Anonimato) ---
    print("\n[Cenário 1] Fluxo de Votação (Duplo-Cego)")
    
    voter = Voter(authority.public_key)
    vote_token = 12345678901234567890 # Ex: Derivado do e-Título + Nonce
    print(f"1. Eleitor gera Token de Voto: {vote_token}")
    
    blinded = voter.blind_message(vote_token)
    print(f"2. Token cegado enviado ao TSE: {str(blinded)[:40]}...")
    
    blinded_sig = authority.sign_blinded(blinded)
    print("3. TSE assina o token sem ver o conteúdo.")
    
    final_sig = voter.unblind_signature(blinded_sig)
    print("4. Eleitor remove a cegueira e obtém a assinatura final.")
    
    is_valid = verify_signature(vote_token, final_sig, authority.public_key)
    print(f"5. Verificação da Urna: {'VÁLIDA - Pode Votar' if is_valid else 'INVÁLIDA'}")

    # --- CENÁRIO 2: Log de Transparência ---
    print("\n[Cenário 2] Log de Transparência (Integridade)")
    log.add_block(["Cand_13", "Cand_22", "Cand_13"])
    log.add_block(["Cand_22", "Cand_22", "Cand_13"])
    
    ok, err_idx = log.verify_integrity()
    print(f"1. Integridade do Log: {'OK' if ok else 'ERRO'}")

    # --- CENÁRIO 3: Software Independence (Detecção de Fraude) ---
    print("\n[Cenário 3] Detecção de Fraude")
    print("1. Atacante altera voto no Bloco 1 de 13 para 22...")
    log.corrupt_block(1, ["Cand_22", "Cand_22", "Cand_13"]) # Mudou o primeiro voto
    
    ok_after, err_idx = log.verify_integrity()
    print(f"2. Integridade após ataque: {'OK' if ok_after else 'FALHA DETECTADA NO BLOCO ' + str(err_idx)}")
    if not ok_after:
        print(">> ALERTA: Fraude Digital Detectada. Iniciando Auditoria Soberana (Papel).")

    # --- CENÁRIO 4: RLA (Risk-Limiting Audit) ---
    print("\n[Cenário 4] Simulação de Auditoria Estatística (RLA)")
    # Ex: Eleição com 100.000 votos. Vencedor com 51% vs 49%.
    # Queremos 99% de confiança (alfa = 0.01)
    margin = 0.02 # 51% - 49%
    alpha = 0.01 # 99% de confiança
    
    # Fórmula simplificada de Bravo para tamanho da amostra
    sample_size = math.ceil(-math.log(alpha) / (2 * (margin**2)))
    print(f"1. Margem de Vitória: {margin*100}%")
    print(f"2. Confiança Desejada: {100*(1-alpha)}%")
    print(f"3. Amostra Necessária de Urnas (RLA): {sample_size} urnas")
    print(">> Com esta amostra, garantimos que qualquer fraude sistêmica seria detectada.")

    print("\n=== Fim da PoC ===")

if __name__ == "__main__":
    run_simulation()
