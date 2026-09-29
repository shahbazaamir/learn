
### Local Modules

Run this command to add local modules 

```shell
export PYTHONPATH="~/git/learn/python3:$PYTHONPATH"
```

Test if Python Path is working :
```shell
python3 test/array_rotation.py
```

### Activate virtual env if you are using any

```shell
source ~/git/learn/.venv/bin/activate
```

### install modules if not available

```shell
uv pip install notebook
```

### Run Jupyter Notebook  

 - export all 
 ```shell
 ../.env/all.txt
 ```
 

```shell

env PYTHONPATH="/Users/zainabfirdaus/git/learn/python3:$PYTHONPATH" jupyter notebook


```


Run local AI agent
```shell
ollama launch opencode --model qwen3:8b
```