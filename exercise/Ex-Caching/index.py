from flask import Flask, render_template, url_for
app = Flask(__name__)

@app.route('/', methods=['GET', 'POST'])
def main(): 
    return render_template('index.html',port =5000)

if __name__ == '__main__':
    app.run(host='localhost',port='5000')
