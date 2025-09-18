
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
source ~/git/airflow/.venv/bin/activate
```

### install modules if not available

```shell
uv pip install notebook
```

### Run Jupyter Notebook - not working

```shell

env PYTHONPATH="/Users/zainabfirdaus/git/learn/python3:$PYTHONPATH" jupyter notebook


```
