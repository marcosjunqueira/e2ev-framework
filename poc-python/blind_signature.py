import hashlib
from cryptography.hazmat.primitives.asymmetric import rsa
from cryptography.hazmat.primitives import serialization

class BlindSignatureAuthority:
    def __init__(self):
        # Gera par de chaves RSA (2048 bits)
        self.private_key = rsa.generate_private_key(
            public_exponent=65537,
            key_size=2048
        )
        self.public_key = self.private_key.public_key()
        
        # Extrai componentes n, e, d para matemática modular
        pub_numbers = self.public_key.public_numbers()
        priv_numbers = self.private_key.private_numbers()
        
        self.n = pub_numbers.n
        self.e = pub_numbers.e
        self.d = priv_numbers.d

    def sign_blinded(self, blinded_message):
        """Assina a mensagem cegada: s' = m'^d mod n"""
        return pow(blinded_message, self.d, self.n)

class Voter:
    def __init__(self, authority_pub_key):
        pub_numbers = authority_pub_key.public_numbers()
        self.n = pub_numbers.n
        self.e = pub_numbers.e
        
    def blind_message(self, message_int):
        """Cega a mensagem: m' = m * r^e mod n"""
        import secrets
        # Fator r aleatório tal que gcd(r, n) = 1
        while True:
            self.r = secrets.randbelow(self.n)
            if self.r > 1 and self.gcd(self.r, self.n) == 1:
                break
        
        # m' = (m * r^e) mod n
        self.blinded = (message_int * pow(self.r, self.e, self.n)) % self.n
        return self.blinded

    def unblind_signature(self, blinded_sig):
        """Remove a cegueira: s = s' * r^-1 mod n"""
        r_inv = pow(self.r, -1, self.n)
        self.signature = (blinded_sig * r_inv) % self.n
        return self.signature

    def gcd(self, a, b):
        while b:
            a, b = b, a % b
        return a

def verify_signature(message_int, signature, authority_pub_key):
    """Verifica: s^e mod n == m"""
    pub_numbers = authority_pub_key.public_numbers()
    n = pub_numbers.n
    e = pub_numbers.e
    return pow(signature, e, n) == message_int
