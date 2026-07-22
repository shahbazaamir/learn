def langchain_agent_flow():
    from langchain.agents import initialize_agent, Tool
    from langchain.agents.agent_types import AgentType
    from langchain.chat_models import ChatOpenAI

    llm = ChatOpenAI(temperature=0)

    def get_current_weather(location: str) -> str:
        """Get the current weather in a given location"""
        return f"The current weather in {location} is 20 degrees Celsius with clear skies."

    tools = [
        Tool(
            name="get_current_weather",
            func=get_current_weather,
            description="Get the current weather in a given location. The input should be a location as a string.",
        )
    ]

    agent = initialize_agent(tools, llm, agent=AgentType.ZERO_SHOT_REACT_DESCRIPTION, verbose=True)

    agent.run("What is the current weather in New York?") 

if __name__ == "__main__":
    langchain_agent_flow()

# with human in the loop
def langgraph_code_review():
    from langgraph import CodeReviewAgent

    code_review_agent = CodeReviewAgent()
    code_review_agent.review_code("def add(a, b): return a + b")
    # human in the loop
    # ... (additional code for human interaction)
    

if __name__ == "__main__":
    langgraph_code_review()
