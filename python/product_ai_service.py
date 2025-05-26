from flask import Flask, request, jsonify
import ollama

app = Flask(__name__)

@app.route("/ask", methods=["POST"])
def ask():
    data = request.get_json()

    instruction = data.get("products")
    question = data.get("listItems")
    print("Received data:", data)
    if not instruction or not question:
        return jsonify({"error": "Missing 'instruction' or 'question'"}), 400


    formatted_products = "\n".join(
    f"- product_id: {item.get('first', 'Unknown')}, product_name: {item.get('second', 'Unknown')}"
    for item in instruction
    )
    print("Formatted products:", formatted_products)
    # Format the shopping list
    formatted_list = "\n".join(f"- {item}" for item in question)
    print("Formatted shopping list:", formatted_list)
    try:
        response = ollama.chat(model='llama3', messages=[
            {"role": "system", "content": "You are a helpful assistant for matching shoplist items to products existing in the shop database. You have to try find a product for every item on a shopping list. You must give back a JSON with objects like this: {\"list_item_name\": \"milk\", \"product_id\": \"99\", \"product_name\": \"Oho milk 4%\",} if you find a match (you have to try hard), the product_name and product_id should be null. Your answer must be only a JSON array with all items from the shopping list. Don't add any other text, just the JSON array."},
            {"role": "system", "content": "You shouldn't search for exact matches, just try to pair products from the databse to products on the list" +  formatted_products},
            {"role": "system", "content": "These are the products, can be found in the database" +  formatted_products},
            {"role": "user", "content": "These are the items from the shopping list (list_item_name): " + formatted_list}
        ])
        return jsonify({"answer": response['message']['content']})
    except Exception as e:
        return jsonify({"error": str(e)}), 500

if __name__ == "__main__":
    app.run(host="0.0.0.0", port=8090)