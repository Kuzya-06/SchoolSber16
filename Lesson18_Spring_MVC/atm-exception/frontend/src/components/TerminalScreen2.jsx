import { useEffect, useState } from "react";
import axios from "axios";
import { toast } from "react-toastify";

const TerminalScreen = ({ pin }) => {
    const [balance, setBalance] = useState(0);
    const [amount, setAmount] = useState("");

    useEffect(() => {
        fetchBalance();
    }, []);

    const fetchBalance = async () => {
        try {
            const res = await axios.get(`http://localhost:8080/api/balance/${pin}`);
            setBalance(res.data);
        } catch (error) {
            toast.error("Ошибка загрузки баланса");
        }
    };

    const handleTransaction = async (type) => {
        try {
            await axios.post(`http://localhost:8080/api/${type}`, { pin, amount: Number(amount) });
            toast.success(`Операция "${type}" выполнена!`);
            fetchBalance();
            setAmount("");
        } catch (error) {
            toast.error(error.response?.data || "Ошибка транзакции");
        }
    };

    return (
        <div className="p-6 bg-white rounded-lg shadow-md">
            <h2 className="text-2xl font-bold text-center">Баланс: {balance} RUB</h2>
            <input
                type="number"
                className="w-full p-2 mt-4 border rounded-md"
                placeholder="Введите сумму"
                value={amount}
                onChange={(e) => setAmount(e.target.value)}
            />
            <div className="flex gap-4 mt-4">
                <button className="flex-1 p-2 text-white bg-green-500 rounded-md" onClick={() => handleTransaction("deposit")}>
                    Пополнить
                </button>
                <button className="flex-1 p-2 text-white bg-red-500 rounded-md" onClick={() => handleTransaction("withdraw")}>
                    Снять
                </button>
            </div>
        </div>
    );
};

export default TerminalScreen;
