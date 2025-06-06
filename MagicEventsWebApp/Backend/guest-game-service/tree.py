from fastapi import FastAPI
from pydantic import BaseModel
from sklearn.tree import DecisionTreeClassifier
import pandas as pd
import json

app = FastAPI()

# Stato globale dell'app
model = None
features = []
target = ""
tree_json = None

class TrainRequest(BaseModel):
    data: list  # lista di dict {feature1: value1, feature2: value2, ..., target: value}
    features: list  # lista di nomi delle feature
    target: str     # nome della colonna target

class PredictRequest(BaseModel):
    input_data: dict  # dict con {feature: value} per prediction

@app.post("/train")
def train(request: TrainRequest):
    global model, features, target, tree_json

    df = pd.DataFrame(request.data)
    features = request.features
    target = request.target

    X = df[features]
    y = df[target]

    # Encode SI/NO -> 1/0 se necessario
    X = X.replace({'SI': 1, 'NO': 0})

    model = DecisionTreeClassifier(criterion="entropy", random_state=13)
    model.fit(X, y)

    tree_json = tree_to_json(model, features)

    return {"message": "Decision tree trained successfully!"}

@app.post("/predict")
def predict(request: PredictRequest):
    if model is None:
        return {"error": "Model not trained yet."}

    input_df = pd.DataFrame([request.input_data])
    input_df = input_df.replace({'SI': 1, 'NO': 0})

    prediction = model.predict(input_df)
    return {"prediction": prediction[0]}

@app.get("/tree")
def get_tree():
    if tree_json is None:
        return {"error": "Tree not trained yet."}
    return tree_json

# Funzione ausiliaria per convertire l'albero in JSON
def tree_to_json(model, feature_names):
    tree_ = model.tree_
    feature_name = [
        feature_names[i] if i != -2 else 'undefined'
        for i in tree_.feature
    ]

    def recurse(node):
        if tree_.feature[node] != -2:
            name = feature_name[node]
            threshold = tree_.threshold[node]
            left = recurse(tree_.children_left[node])
            right = recurse(tree_.children_right[node])
            return {
                'name': name,
                'threshold': threshold,
                'left': left,
                'right': right
            }
        else:
            return {
                'value': model.classes_[tree_.value[node].argmax()]
            }
    return recurse(0)
