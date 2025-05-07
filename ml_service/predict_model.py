import numpy as np
from PIL import Image
from tensorflow.keras.models import load_model
from tensorflow.keras.applications.vgg16 import preprocess_input
from tensorflow.keras.preprocessing.image import img_to_array
import pickle
import sys
import os
import logging

# 禁用所有 TensorFlow 日志
os.environ['TF_CPP_MIN_LOG_LEVEL'] = '3'  # FATAL
logging.getLogger('tensorflow').setLevel(logging.ERROR)
import tensorflow as tf
tf.get_logger().setLevel('ERROR')

def get_script_dir():
    return os.path.dirname(os.path.abspath(__file__))

def load_resources():
    try:
        script_dir = get_script_dir()
        model_path = os.path.join(script_dir, "vgg_model.h5")
        encoder_path = os.path.join(script_dir, "label_encoder.pkl")
        
        model = load_model(model_path)
        with open(encoder_path, 'rb') as f:
            label_encoder = pickle.load(f)
            
        return model, label_encoder
    except Exception as e:
        print(f"Error loading resources: {str(e)}", file=sys.stderr)
        sys.exit(1)

# 加载模型和标签编码器
model, label_encoder = load_resources()

def predict_image(img_path, img_size=(64, 64)):
    try:
        img = Image.open(img_path).convert('RGB')
        img = img.resize(img_size)
        img_array = img_to_array(img) / 255.0  # 归一化
        img_array = np.expand_dims(img_array, axis=0)  # 添加 batch 维度
        
        pred = model.predict(img_array, verbose=0)  # 禁用预测进度输出
        class_index = np.argmax(pred, axis=1)[0]
        confidence = float(pred[0][class_index])
        class_label = label_encoder.inverse_transform([class_index])[0]
        
        # 只输出最终结果，格式：情绪:置信度
        print(f"{class_label}:{confidence}")
        return class_label, confidence
    except Exception as e:
        print(f"Error during prediction: {str(e)}", file=sys.stderr)
        sys.exit(1)

if __name__ == "__main__":
    if len(sys.argv) != 2:
        print("Usage: python predict_model.py <image_path>", file=sys.stderr)
        sys.exit(1)
    
    image_path = sys.argv[1]
    if not os.path.exists(image_path):
        print(f"Error: Image file not found: {image_path}", file=sys.stderr)
        sys.exit(1)
    
    predict_image(image_path)