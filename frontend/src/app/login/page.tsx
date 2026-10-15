"use client";

import { useState } from "react";
import Link from "next/link";
import Image from "next/image";
import { useRouter } from "next/navigation";
import { User } from "lucide-react";

const API_URL = process.env.NEXT_PUBLIC_API_URL ?? "http://localhost:8080";

// Rota da página do Hub (a segunda page.tsx). Ajuste para o nome da sua pasta.
const ROTA_APOS_LOGIN = "/home_c/login";

export default function LoginPage() {
  const router = useRouter();

  const [identifier, setIdentifier] = useState("");
  const [password, setPassword] = useState("");
  const [erro, setErro] = useState<string | null>(null);
  const [carregando, setCarregando] = useState(false);

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    if (carregando) return;

    setErro(null);
    setCarregando(true);

    try {
      const resposta = await fetch(`${API_URL}/auth/login`, {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        // Necessário para o navegador aceitar o cookie com o JWT enviado pelo backend
        credentials: "include",
        body: JSON.stringify({
          email: identifier.trim(),
          senha: password,
        }),
      });

      if (resposta.ok) {
        router.replace(ROTA_APOS_LOGIN);
        return;
      }

      if (resposta.status === 401 || resposta.status === 403) {
        setErro("E-mail ou senha incorretos.");
      } else if (resposta.status === 400) {
        setErro("Confira se o e-mail e a senha foram preenchidos corretamente.");
      } else {
        setErro("Não foi possível entrar agora. Tente novamente em instantes.");
      }
    } catch {
      setErro(
        "Não foi possível conectar ao servidor. Verifique se o backend está rodando."
      );
    } finally {
      setCarregando(false);
    }
  };

  return (
    <div className="min-h-screen bg-[#f1f5f9] text-gray-900 flex flex-col justify-between font-sans">
      {/* Cabeçalho */}
      <header className="w-full flex justify-between items-center px-6 sm:px-8 py-5">
        <Link
          href="/"
          className="flex items-center group transition-opacity hover:opacity-90"
        >
          <Image
            src="/logo_CampusHub_sem_fundo.png"
            alt="Logo CampusHub"
            width={160}
            height={40}
            className="h-9 w-auto object-contain"
            priority
          />
        </Link>
        <span className="text-gray-900 text-sm font-medium">
          Instituto Mauá de Tecnologia
        </span>
      </header>

      {/* Conteúdo Central / Card de Login */}
      <main className="flex-1 flex items-center justify-center px-4 py-8">
        <div className="w-full max-w-md bg-white border border-gray-400/80 rounded-2xl p-8 shadow-sm flex flex-col items-center">
          {/* Ícone de Usuário */}
          <div className="w-20 h-20 rounded-full bg-[#4285f4] flex items-center justify-center text-white mb-4 shadow-sm">
            <User size={48} strokeWidth={2.5} />
          </div>

          {/* Título de Boas-Vindas */}
          <h1 className="text-2xl font-bold text-gray-900 mb-8 tracking-tight text-center">
            Bem Vindo ao CampusHub!
          </h1>

          {/* Formulário */}
          <form onSubmit={handleSubmit} className="w-full flex flex-col gap-4">
            {/* Campo E-mail / RA */}
            <div className="flex flex-col gap-1.5">
              <label
                htmlFor="identifier"
                className="text-base font-semibold text-gray-900"
              >
                Email
              </label>
              <input
                id="identifier"
                type="text"
                value={identifier}
                onChange={(e) => setIdentifier(e.target.value)}
                placeholder="exemplo@maua.br"
                autoComplete="username"
                className="w-full bg-[#b0b0b0] text-gray-800 placeholder-gray-600 px-4 py-2.5 rounded-lg outline-none focus:ring-2 focus:ring-blue-500 text-sm"
                required
              />
            </div>

            {/* Campo Senha */}
            <div className="flex flex-col gap-1.5">
              <label
                htmlFor="password"
                className="text-base font-semibold text-gray-900"
              >
                Senha
              </label>
              <input
                id="password"
                type="password"
                value={password}
                onChange={(e) => setPassword(e.target.value)}
                placeholder="Digite sua senha"
                autoComplete="current-password"
                className="w-full bg-[#b0b0b0] text-gray-800 placeholder-gray-600 px-4 py-2.5 rounded-lg outline-none focus:ring-2 focus:ring-blue-500 text-sm"
                required
              />
            </div>

            {/* Esqueci a Senha */}
            <div className="flex justify-start">
              <Link
                href="/recuperar-senha"
                className="text-sm font-medium text-[#4a80e2] hover:underline"
              >
                Esqueceu a senha?
              </Link>
            </div>

            {/* Mensagem de erro */}
            {erro && (
              <p
                role="alert"
                className="text-sm font-medium text-red-700 bg-red-50 border border-red-200 rounded-lg px-3 py-2"
              >
                {erro}
              </p>
            )}

            {/* Botão de Login */}
            <button
              type="submit"
              disabled={carregando}
              className="w-full mt-2 bg-[#2f6ff7] hover:bg-[#255cd4] disabled:opacity-60 disabled:cursor-not-allowed text-white font-semibold py-3 rounded-xl transition duration-150 shadow-sm text-lg cursor-pointer"
            >
              {carregando ? "Entrando..." : "Login"}
            </button>
          </form>

          {/* Divisor "ou" */}
          <div className="w-full flex items-center my-5">
            <div className="flex-1 border-t border-gray-400"></div>
            <span className="px-3 text-sm text-gray-700 font-medium">ou</span>
            <div className="flex-1 border-t border-gray-400"></div>
          </div>

          {/* Link para Criar Conta */}
          <Link
            href="/criar_conta"
            className="text-base font-bold text-[#0c1b40] hover:underline"
          >
            Criar Conta
          </Link>
        </div>
      </main>

      {/* Espaçador inferior */}
      <footer className="h-6"></footer>
    </div>
  );
}
