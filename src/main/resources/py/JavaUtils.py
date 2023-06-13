import tiktoken
from typing import ( List )


def tokenLen(ss: str, model_name: str = "gpt-3.5-turbo-0301") -> int:
    encoding = tiktoken.encoding_for_model(model_name)
    return len(encoding.encode(ss))

def tokenIds(text: str,model_name:str="gpt-3.5-turbo-0301") -> List[int]:
    encoding = tiktoken.encoding_for_model(model_name)
    return encoding.encode(text)
