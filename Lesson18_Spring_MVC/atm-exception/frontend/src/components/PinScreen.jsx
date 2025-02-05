import { useState } from "react";
import axios from "axios";
import { toast } from "react-toastify";

export const PinScreen = ({ onAuthSuccess }) => {
    const [card, setCard] = useState("");
    const [pin, setPin] = useState("");
    const [errorMessage, setErrorMessage] = useState(""); // Сохраняем сообщение об ошибке

    const handlePinSubmit = async () => {
        try {
            const res = await axios.post("http://localhost:8080/api/validate-pin", { card, pin }, {
                headers: {
                    "Content-Type": "application/json"
                }
            });
            console.log(res.data)

            if (res.data === "Доступ разрешен") {
                toast.success(res.data);
                setErrorMessage(""); // Сбрасываем ошибку при успешном входе
                onAuthSuccess(card);
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
        <div style={styles.box}>
        <div style={styles.container}>
            <h2 style={styles.title}>Введите Номер карты. 6 цифр</h2>
            <input
                type="text"
                style={styles.input}
                maxLength={6}
                value={card}
                onChange={(e) => setCard(e.target.value)}
            />
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
        </div>
    );
};

const styles = {
box: {
    display: "flex",
    justifyContent: "center",
    alignItems: "center",
    minHeight: "100vh",
    minWidth: "100vw"
},
    container: {
        width: "450px",
        padding: "10px",
        margin: "50px auto",
        textAlign: "center",
        backgroundColor: "#fff",
        borderRadius: "8px",
        boxShadow: "0px 0px 10px rgba(0, 0, 0, 0.1)"
    },
    title: {
        fontSize: "20px",
        fontWeight: "bold",
        marginBottom: "15px",
        color: "#007bff"
    },
    input: {
        width: "90%",
        padding: "10px",
        fontSize: "18px",
        textAlign: "center",
        border: "1px solid #ccc",
        borderRadius: "4px",
        color: "#00f"
    },
    button: {
        width: "100%",
        padding: "10px",
        marginTop: "15px",
        backgroundColor: "#007bff",
        color: "#00f",
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

