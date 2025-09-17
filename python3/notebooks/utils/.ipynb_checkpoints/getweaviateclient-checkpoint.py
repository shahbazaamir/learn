import weaviate
from weaviate.classes.init import Auth
import os

def getclient2() :
# Best practice: store your credentials in environment variables
    wcd_url = "https://pxbcoxesxsrbwqq0dxm0g.c0.us-west3.gcp.weaviate.cloud" #os.environ["WCD_URL"]
    wcd_api_key = "8eUm8GnrYJGQtSttgzkHXcXTBt9YH6lBW6oL" #os.environ["WCD_API_KEY"]

    client = weaviate.connect_to_weaviate_cloud(
        cluster_url=wcd_url,                                    # Replace with your Weaviate Cloud URL
        auth_credentials=Auth.api_key(wcd_api_key),             # Replace with your Weaviate Cloud key
    )
    return client
#    print(client.is_ready())  # Should print: `True`

#client.close()  # Free up resources