const video = document.getElementById("video");
let lastGender = "";
let lastAge = -1;

Promise.all([
    faceapi.nets.tinyFaceDetector.loadFromUri("./models"),
    faceapi.nets.faceLandmark68Net.loadFromUri("./models"),
    faceapi.nets.faceRecognitionNet.loadFromUri("./models"),
    faceapi.nets.ageGenderNet.loadFromUri("./models"),
]).then(webCam);

function webCam() {
    navigator.mediaDevices
        .getUserMedia({
            video: true,
            audio: false,
        })
        .then((stream) => {
            video.srcObject = stream;
        })
        .catch((error) => {
            console.log(error);
        });
}

video.addEventListener("play", () => {
    const canvas = faceapi.createCanvasFromMedia(video);
    document.body.append(canvas);

    faceapi.matchDimensions(canvas, { height: video.height, width: video.width });

    setInterval(async () => {
        const detection = await faceapi
            .detectAllFaces(video, new faceapi.TinyFaceDetectorOptions())
            .withFaceLandmarks()
            .withAgeAndGender();

        canvas.getContext("2d").clearRect(0, 0, canvas.width, canvas.height);

        const resizedWindow = faceapi.resizeResults(detection, {
            height: video.height,
            width: video.width,
        });

        faceapi.draw.drawDetections(canvas, resizedWindow);
        faceapi.draw.drawFaceLandmarks(canvas, resizedWindow);

        resizedWindow.forEach((det) => {
            const box = det.detection.box;
            const age = Math.round(det.age);
            const gender = det.gender;

            // Verifica se o gênero ou a idade mudou
            if (lastGender !== gender || lastAge !== age) {
                lastGender = gender;
                lastAge = age;

                // Gera a ficha 
                let ficha = generateFicha(gender, age);
                document.getElementById("ficha-content").innerHTML = ficha;
                document.getElementById("ficha-container").style.display = "block";
            }

            const drawBox = new faceapi.draw.DrawBox(box, {
                label: `${age} years old, ${gender}`,
            });
            drawBox.draw(canvas);
        });
    }, 100);
});

function generateFicha(gender, age) {
    let faixaEtaria = "";
    if (age >= 15 && age <= 30) {
        faixaEtaria = "15-30";
    } else if (age >= 31 && age <= 45) {
        faixaEtaria = "31-45";
    } else if (age >= 46 && age <= 60) {
        faixaEtaria = "46-60";
    } else if (age > 60) {
        faixaEtaria = "60+";
    } else {
        return "<p>Idade fora da faixa definida.</p>";
    }

    let ficha = "";
    if (faixaEtaria === "15-30") {
        ficha = gender === "male" ? `
            <h3>Ficha Masculina - 15 a 30 anos</h3>
            <p>Exercícios:</p>
            <ul>
                <li>Supino reto: 4 séries de 10-12 repetições</li>
                <li>Agachamento: 4 séries de 10-12 repetições</li>
                <li>Remada curvada: 4 séries de 10-12 repetições</li>
                <li>Desenvolvimento de ombros: 4 séries de 10-12 repetições</li>
                <li>Abdômen (prancha): 3 séries de 45-60 segundos</li>
            </ul>
        ` : `
            <h3>Ficha Feminina - 15 a 30 anos</h3>
            <p>Exercícios:</p>
            <ul>
                <li>Agachamento com halteres: 4 séries de 12-15 repetições</li>
                <li>Leg Press: 4 séries de 12-15 repetições</li>
                <li>Remada unilateral: 4 séries de 12-15 repetições</li>
                <li>Desenvolvimento de ombros: 4 séries de 12-15 repetições</li>
                <li>Prancha: 3 séries de 30-45 segundos</li>
            </ul>
        `;
    } else if (faixaEtaria === "31-45") {
        ficha = gender === "male" ? `
            <h3>Ficha Masculina - 31 a 45 anos</h3>
            <p>Exercícios:</p>
            <ul>
                <li>Supino inclinado: 3 séries de 8-10 repetições</li>
                <li>Agachamento: 3 séries de 8-10 repetições</li>
                <li>Barra fixa: 3 séries até a falha</li>
                <li>Levantamento terra: 3 séries de 8-10 repetições</li>
                <li>Abdômen (crunch): 3 séries de 15-20 repetições</li>
            </ul>
        ` : `
            <h3>Ficha Feminina - 31 a 45 anos</h3>
            <p>Exercícios:</p>
            <ul>
                <li>Agachamento com barra: 3 séries de 10-12 repetições</li>
                <li>Leg Press: 3 séries de 10-12 repetições</li>
                <li>Puxada no pulley: 3 séries de 10-12 repetições</li>
                <li>Elevação de quadril: 3 séries de 12-15 repetições</li>
                <li>Prancha: 3 séries de 30-45 segundos</li>
            </ul>
        `;
    } else if (faixaEtaria === "46-60") {
        ficha = gender === "male" ? `
            <h3>Ficha Masculina - 46 a 60 anos</h3>
            <p>Exercícios:</p>
            <ul>
                <li>Supino reto: 3 séries de 8-10 repetições</li>
                <li>Agachamento com barra: 3 séries de 8-10 repetições</li>
                <li>Remada baixa: 3 séries de 8-10 repetições</li>
                <li>Desenvolvimento de ombros: 3 séries de 8-10 repetições</li>
                <li>Abdômen (prancha lateral): 3 séries de 20-30 segundos de cada lado</li>
            </ul>
        ` : `
            <h3>Ficha Feminina - 46 a 60 anos</h3>
            <p>Exercícios:</p>
            <ul>
                <li>Agachamento com halteres: 3 séries de 10-12 repetições</li>
                <li>Leg Press: 3 séries de 10-12 repetições</li>
                <li>Remada baixa: 3 séries de 10-12 repetições</li>
                <li>Elevação de quadril: 3 séries de 10-12 repetições</li>
                <li>Prancha: 3 séries de 25-30 segundos</li>
            </ul>
        `;
    } else if (faixaEtaria === "60+") {
        ficha = gender === "male" ? `
            <h3>Ficha Masculina - 60 anos ou mais</h3>
            <p>Exercícios:</p>
            <ul>
                <li>Supino reto: 2 séries de 8-10 repetições</li>
                <li>Agachamento com barra: 2 séries de 8-10 repetições</li>
                <li>Remada baixa: 2 séries de 8-10 repetições</li>
                <li>Desenvolvimento de ombros: 2 séries de 8-10 repetições</li>
                <li>Exercícios de mobilidade: 3 séries de 12-15 repetições</li>
            </ul>
        ` : `
            <h3>Ficha Feminina - 60 anos ou mais</h3>
            <p>Exercícios:</p>
            <ul>
                <li>Agachamento com halteres: 2 séries de 10-12 repetições</li>
                <li>Leg Press: 2 séries de 10-12 repetições</li>
                <li>Remada baixa: 2 séries de 10-12 repetições</li>
                <li>Desenvolvimento de ombros: 2 séries de 10-12 repetições</li>
                <li>Exercícios de mobilidade: 3 séries de 12-15 repetições</li>
            </ul>
        `;
    }

    return ficha;
}