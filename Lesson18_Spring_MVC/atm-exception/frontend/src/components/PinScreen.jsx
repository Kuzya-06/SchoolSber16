import { useState } from "react";
import axios from "axios";
import { toast } from "react-toastify";

const PinScreen = ({ onAuthSuccess }) => {
    const [pin, setPin] = useState("");
    const [errorMessage, setErrorMessage] = useState(""); // Сохраняем сообщение об ошибке

    const handlePinSubmit = async () => {
        try {
            const res = await axios.post("http://localhost:8080/api/validate-pin", { pin });

            if (res.data === "Доступ разрешен") {
                toast.success(res.data);
                setErrorMessage(""); // Сбрасываем ошибку при успешном входе
                onAuthSuccess();
            } else {
                setErrorMessage(res.data); // Показываем сообщение ошибки
                toast.error(res.data);
            }
        } catch (error) {
            const serverMessage = error.response?.data || "Ошибка сервера";
            setErrorMessage(serverMessage);
            toast.error(serverMessage);
        }
    };

    return (
        <div style={styles.container}>
            <h2 style={styles.title}>Введите PIN</h2>
            <input
                type="password"
                style={styles.input}
                maxLength={4}
                value={pin}
                onChange={(e) => setPin(e.target.value)}
            />
            <button style={styles.button} onClick={handlePinSubmit}>
                Войти
            </button>
            {errorMessage && <p style={styles.error}>{errorMessage}</p>} {/* Вывод ошибки */}
        </div>
    );
};

const styles = {
    container: {
        width: "300px",
        padding: "20px",
        margin: "100px auto",
        textAlign: "center",
        backgroundColor: "#fff",
        borderRadius: "8px",
        boxShadow: "0px 0px 10px rgba(0, 0, 0, 0.1)"
    },
    title: {
        fontSize: "20px",
        fontWeight: "bold",
        marginBottom: "15px"
    },
    input: {
        width: "100%",
        padding: "10px",
        fontSize: "18px",
        textAlign: "center",
        border: "1px solid #ccc",
        borderRadius: "4px"
    },
    button: {
        width: "100%",
        padding: "10px",
        marginTop: "15px",
        backgroundColor: "#007bff",
        color: "#fff",
        fontSize: "16px",
        border: "none",
        borderRadius: "4px",
        cursor: "pointer"
    },
    error: {
        color: "red",
        marginTop: "10px",
        fontSize: "14px"
    }
};

export default PinScreen;
