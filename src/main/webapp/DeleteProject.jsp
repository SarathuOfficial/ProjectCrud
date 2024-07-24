<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Delete Project</title>
    <style>
        body {
            font-family: Arial, sans-serif;
            text-align: center;
            background-color: #f0f0f0;
            margin: 0;
            padding: 0;
        }
        .container {
            margin: 100px auto;
            background-color: white;
            padding: 30px;
            border-radius: 10px;
            box-shadow: 0 4px 8px rgba(0, 0, 0, 0.2);
            max-width: 400px;
            width: 100%;
        }
        h1 {
            margin: 0 0 20px;
            color: #333;
        }
        label {
            display: block;
            font-size: 18px;
            margin-bottom: 8px;
            color: #555;
        }
        input {
            padding: 12px;
            font-size: 16px;
            border: 1px solid #ccc;
            border-radius: 5px;
            width: calc(100% - 24px);
            margin: 0 auto;
            display: block;
        }
        button {
            padding: 12px;
            font-size: 16px;
            background-color: #f44336;
            color: white;
            border: none;
            border-radius: 5px;
            cursor: pointer;
            transition: background-color 0.3s ease;
            margin-top: 10px;
        }
        button:hover {
            background-color: #e53935;
        }
        #result {
            margin-top: 20px;
            font-size: 16px;
            color: #333;
        }
    </style>
    <script>
    function sendDeleteRequest() {
        const projectId = document.getElementById('projectId').value; // Get the project ID from the input field

        fetch('/ProjectCRUD/DeleteProject', {
            method: 'DELETE',
            headers: {
                'Content-Type': 'text/plain',
            },
            body: projectId
        })
        .then(response => {
            if (response.ok) {
                return response.text();
            } else {
                throw new Error('Failed to delete project');
            }
        })
        .then(data => {
            document.getElementById('result').innerText = data;
        })
        .catch(error => {
            document.getElementById('result').innerText = 'Error: ' + error.message;
        });
    }
    </script>
</head>
<body>
    <div class="container">
        <h1>Delete Project</h1>
        <label for="projectId">Project ID</label>
        <input type="text" id="projectId" name="projectId" required placeholder="Enter Project ID">
        <button type="button" onclick="sendDeleteRequest()">Delete Project</button>
        <p id="result"></p>
    </div>
</body>
</html>
