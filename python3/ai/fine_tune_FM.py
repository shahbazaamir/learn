def finetine_FM():
    # fine tune a foundation model using langchain
    from langchain import OpenAI
    from langchain.chains import LLMChain
    from langchain.prompts import PromptTemplate    
    llm = OpenAI(temperature=0.9)
    prompt = PromptTemplate.from_template("What year did {company} was founded?")
    chain = LLMChain(llm=llm, prompt=prompt)
    print(chain.run(company="Google"))  
if __name__ == "__main__":
    finetine_FM()
    