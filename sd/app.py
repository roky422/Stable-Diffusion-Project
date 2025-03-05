from flask import Flask, request, jsonify, session
from flask_session import Session
import base64
from io import BytesIO
from PIL import Image
from Demo import generate_image, image_to_text, image_to_image

app = Flask(__name__)
# app.secret_key = 'supersecretkey'
# app.config["SESSION_PERMANENT"] = False
# app.config["SESSION_TYPE"] = "filesystem"
main_model = None
naruto_model = None
# Session(app)

@app.route('/select_model', methods=['POST'])
def select_model():
    global main_model, naruto_model
    data = request.get_json()
    main_model = data.get('mainModel')
    naruto_model = data.get('narutoModel')
    print(f"Received model selection: Main Model = {main_model}, Naruto Model = {naruto_model}")

    return jsonify({'message': 'Model selection received successfully'}), 200

@app.route('/generate_image', methods=['POST'])
def generate_image_endpoint():
    global main_model, naruto_model
    data = request.get_json()
    prompt = data.get('prompt')

    if not prompt:
        return jsonify({'error': 'Prompt is required'}), 400

    # main_model = session.get("main_model")
    # naruto_model = session.get("naruto_model")
    # print(session.get("main_model"))
    # print(session.get("naruto_model"))
    print(main_model)
    print(naruto_model)
    if main_model == 'FN Naruto':
        image = Image.fromarray(generate_image(prompt, naruto_model))
    else: 
        image = Image.fromarray(generate_image(prompt, main_model))
    # Dummy image generation for demonstration
    #image = Image.new('RGB', (256, 256), color = 'red')

    buffered = BytesIO()
    image.save(buffered, format="JPEG")
    img_str = base64.b64encode(buffered.getvalue()).decode()

    return jsonify({'image': img_str})


@app.route('/send_text', methods=['POST'])
def send_text():
    if 'image' not in request.files:
        return jsonify({'error': 'Image is required'}), 400

    image = request.files['image']

    text = image_to_text(image)
    # text = "A cat sitting on a table"

    return jsonify({'text': text})


@app.route('/send_image', methods=['POST'])
def send_image():
    if 'image' not in request.files:
        return jsonify({'error': 'Image is required'}), 400

    image = request.files['image']
    image = Image.open(image)
    print(type(image))

    new_image = image_to_image(image)

    buffered = BytesIO()
    new_image.save(buffered, format="JPEG")
    img_str = base64.b64encode(buffered.getvalue()).decode()

    return jsonify({'image': img_str})


if __name__ == '__main__':
    app.run(debug=True, host='0.0.0.0', port=5000)
