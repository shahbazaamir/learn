from flask import Flask, request, jsonify

app = Flask(__name__)

# This is our in-memory list to store data
data_store = []

@app.route('/data', methods=['GET'])
def get_data():
    """Returns the entire list."""
    return jsonify({"list": data_store}), 200

@app.route('/init', methods=['POST'])
def init_data():
    content = request.json
    if not content or 'people' not in content:
        return jsonify({"error": "Missing 'people' in request body"}), 400
    global data_store
    data_store = [0] * int(content['people'])
    return jsonify({"message": "Initialized successfully", "current_list": data_store}), 201

@app.route('/add', methods=['PUT'])
def add_data():
    content = request.json
    if not content or 'exclude' not in content:
        return jsonify({"error": "Missing 'exclude' in request body"}), 400
    if not content or 'count' not in content:
        return jsonify({"error": "Missing 'count' in request body"}), 400
    cnt = int(content['count'])
    exc = int(content['exclude'])
    for i in range(0,len(data_store)) :
        if i != exc :
            data_store[i] += cnt
    return jsonify({"message": "Updated successfully", "current_list": data_store}), 200

@app.route('/clear', methods=['PUT'])
def clear_data():
    global data_store
    data_store = []
    return jsonify({"message": "Updated successfully", "current_list": data_store}), 200

@app.route('/data', methods=['POST'])
def put_data():
    """Adds a new item to the list via JSON body."""
    content = request.json
    if not content or 'item' not in content:
        return jsonify({"error": "Missing 'item' in request body"}), 400
    
    # Use .append() to add data to the list
    data_store.append(content['item'])
    return jsonify({"message": "Added successfully", "current_list": data_store}), 201

if __name__ == '__main__':
    app.run(debug=True)
