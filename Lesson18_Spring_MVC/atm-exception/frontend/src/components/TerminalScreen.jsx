import { useState, useEffect } from "react";
import axios from "axios";
import { toast } from "react-toastify";


export const TerminalScreen = ({ card }) => {
    const [balance, setBalance] = useState(0);
    const [amount, setAmount] = useState("");
    const [errorMessage, setErrorMessage] = useState("");  // Для хранения ошибок

    useEffect(() => {
        fetchBalance();
    }, []);

    const fetchBalance = async () => {
        if (!card) {
            toast.error("Ошибка: номер карты не найден!");
            return;
        }
        try {
            const res = await axios.get(`http://localhost:8080/api/balance/${card}`); // ✅ Передаем карту
            console.log(card)
            setBalance(res.data);
            setErrorMessage("");  // Сбрасываем ошибку при успешном запросе
        } catch (error) {
            console.log(error.response)
            setErrorMessage(error.response?.data || "Ошибка загрузки баланса");
            toast.error(error.response?.data || "Ошибка загрузки баланса");
        }
    };

    const handleTransaction = async (typeOperation) => {
        try {
            const response = await axios.post(`http://localhost:8080/api/${typeOperation}`, { card, amount: Number(amount) });

          console.log(response.data)

          if(response.data === `Операция выполнена!`) {
              toast.success(`Операция "${typeOperation}" выполнена!`);
              fetchBalance();
              setAmount("");  // Очистить поле после успешной операции
              setErrorMessage("");  // Сбрасываем ошибку при успешной операции
          } else {
              setErrorMessage(response.data);
              toast.error(response.data);
          }
        } catch (error) {
            console.log(error.response)
            // Добавляем обработку ошибок и вывод их на экран
            const errorMessage = error.response?.data || "Ошибка транзакции";
           console.log(errorMessage)
            setErrorMessage(errorMessage);  // Сохраняем ошибку для отображения
            toast.error(errorMessage);  // Показываем ошибку через toast
        }
    };

    return (
        <div style={styles.box}>
        <div style={styles.container}>
            <h2 style={styles.title}>Баланс: {balance} RUB</h2>
            <input
                type="number"
                style={styles.input}
                placeholder="Введите сумму"
                value={amount}
                onChange={(e) => setAmount(e.target.value)}
            />
            <div style={styles.containerButton}>
                <button style={styles.button} onClick={() => handleTransaction("deposit")}>
                    Пополнить
                </button>


                <button style={styles.button} onClick={() => handleTransaction("withdraw")}>
                    Снять
                </button>
                    </div>
            {errorMessage && <p style={styles.error}>{errorMessage}</p>}  {/* Показываем ошибку на экране */}
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
    containerButton: {
        display: "flex",  // Выравниваем кнопки в ряд
        justifyContent: "space-between", // Распределяем кнопки равномерно
        alignItems: "center",
        marginTop: "20px",
        gap: "20px"  // Добавляем отступ между кнопками
    },
    button: {
        flex: "1",  // Кнопки будут равной ширины
        padding: "10px",
        backgroundColor: "#007bff",
        color: "#fff",
        fontSize: "16px",
        border: "none",
        borderRadius: "4px",
        cursor: "pointer",
        textAlign: "center"
    },
    error: {
        color: "red",
        marginTop: "10px",
        fontSize: "14px"
    }
};