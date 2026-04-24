import hashlib

class Block:
    def __init__(self, previous_hash, content):
        self.previous_hash = previous_hash
        self.content = content
        self.hash = self.calculate_hash()

    def calculate_hash(self):
        sha = hashlib.sha256()
        sha.update(str(self.previous_hash).encode('utf-8'))
        sha.update(str(self.content).encode('utf-8'))
        return sha.hexdigest()

class TransparencyLog:
    def __init__(self):
        self.chain = [Block("0", "GENESIS")]

    def add_block(self, votes):
        previous_hash = self.chain[-1].hash
        new_block = Block(previous_hash, votes)
        self.chain.append(new_block)

    def verify_integrity(self):
        for i in range(1, len(self.chain)):
            current = self.chain[i]
            previous = self.chain[i-1]
            
            if current.previous_hash != previous.hash:
                return False, i
            if current.hash != current.calculate_hash():
                return False, i
        return True, -1

    def corrupt_block(self, index, new_content):
        if 0 <= index < len(self.chain):
            self.chain[index].content = new_content
            # Note: we don't recalculate the hash, to simulate unauthorized change
