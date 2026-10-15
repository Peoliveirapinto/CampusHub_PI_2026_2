"use client";

import { useState } from "react";
import Link from "next/link";
import Image from "next/image";
import { useRouter } from "next/navigation";
import { ChevronDown } from "lucide-react";

const API_URL = process.env.NEXT_PUBLIC_API_URL ?? "http://localhost:8080";

// Chave usada para levar nome e curso até o Passo 2 (criar_perfil).
// A senha NUNCA é guardada aqui.
const CHAVE_DADOS_CADASTRO = "campushub:dadosCadastro";

export default function CriarContaPage() {
  const router = useRouter();

  const [nome, setNome] = useState("");
  const [email, setEmail] = useState("");
  const [curso, setCurso] = useState("");
  const [senha, setSenha] = useState("");
  const [erro, setErro] = useState<string | null>(null);
  const [carregando, setCarregando] = useState(false);

  const handleSubmit = async (e: React.FormEvent<HTMLFormElement>) => {
    e.preventDefault();
    if (carregando) return;

    setErro(null);
    setCarregando(true);

    const emailLimpo = email.trim().toLowerCase();

    try {
      // 1) Cria o usuário (o backend recebe apenas e-mail e senha)
      const respostaCadastro = await fetch(`${API_URL}/auth/cadastro`, {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        credentials: "include",
        body: JSON.stringify({ email: emailLimpo, senha }),
      });

      if (!respostaCadastro.ok) {
        if (respostaCadastro.status === 409) {
          setErro("Já existe uma conta com esse e-mail. Tente fazer login.");
        } else if (respostaCadastro.status === 400) {
          setErro("Confira o e-mail e a senha (mínimo de 8 caracteres).");
        } else {
          setErro("Não foi possível criar a conta agora. Tente novamente.");
        }
        return;
      }

      // 2) Faz login automático para que o Passo 2 já tenha o cookie com o JWT
      const respostaLogin = await fetch(`${API_URL}/auth/login`, {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        credentials: "include",
        body: JSON.stringify({ email: emailLimpo, senha }),
      });

      if (!respostaLogin.ok) {
        // A conta foi criada, só o login automático falhou
        router.replace("/login");
        return;
      }

      // O login devolve o id do usuário, que o Passo 2 precisa para criar o perfil
      let usuarioId: string | undefined;
      try {
        const usuarioLogado = await respostaLogin.json();
        usuarioId = usuarioLogado?.id;
      } catch {
        // sem id, o Passo 2 pedirá para refazer o Passo 1
      }

      // 3) Guarda nome, curso e id para o Passo 2 e segue
      try {
        sessionStorage.setItem(
          CHAVE_DADOS_CADASTRO,
          JSON.stringify({ nome: nome.trim(), curso, usuarioId })
        );
      } catch {
        // sessionStorage indisponível: o Passo 2 simplesmente não vem pré-preenchido
      }

      router.push("/criar_perfil");
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

      {/* Conteúdo Central / Card de Cadastro */}
      <main className="flex-1 flex items-center justify-center px-4 py-8">
        <div className="w-full max-w-md bg-white border border-gray-400/80 rounded-2xl p-8 shadow-sm">
          {/* Topo do Card */}
          <div className="flex justify-between items-start mb-2">
            <div>
              <h1 className="text-2xl font-bold text-gray-900">Criar conta</h1>
              <p className="text-xs text-gray-600 mt-0.5 italic">
                Faça parte da rede acadêmica da Mauá
              </p>
            </div>
            <span className="bg-[#cbe8fd] text-[#0284c7] text-xs font-semibold px-3 py-1 rounded-full whitespace-nowrap">
              Passo 1 de 2
            </span>
          </div>

          <div className="border-t border-gray-300 my-4"></div>

          {/* Formulário */}
          <form onSubmit={handleSubmit} className="flex flex-col gap-4">
            {/* Nome Completo */}
            <div className="flex flex-col gap-1">
              <label
                htmlFor="nome"
                className="text-xs font-semibold text-gray-800 tracking-wider"
              >
                NOME COMPLETO
              </label>
              <input
                id="nome"
                type="text"
                placeholder="Ex: Mario Bros"
                value={nome}
                onChange={(e) => setNome(e.target.value)}
                autoComplete="name"
                className="w-full bg-[#dedede] text-gray-800 placeholder-gray-500 px-4 py-2.5 rounded-xl outline-none focus:ring-2 focus:ring-blue-500 text-sm"
                required
              />
            </div>

            {/* E-mail Institucional */}
            <div className="flex flex-col gap-1">
              <label
                htmlFor="email"
                className="text-xs font-semibold text-gray-800 tracking-wider"
              >
                E-MAIL INSTITUCIONAL
              </label>
              <input
                id="email"
                type="email"
                placeholder="Ex: 21.1111-1@maua.br"
                value={email}
                onChange={(e) => setEmail(e.target.value)}
                autoComplete="email"
                className="w-full bg-[#dedede] text-gray-800 placeholder-gray-500 px-4 py-2.5 rounded-xl outline-none focus:ring-2 focus:ring-blue-500 text-sm"
                required
              />
            </div>

            {/* Curso */}
            <div className="flex flex-col gap-1">
              <label
                htmlFor="curso"
                className="text-xs font-semibold text-gray-800 tracking-wider"
              >
                CURSO
              </label>
              <div className="relative">
                <select
                  id="curso"
                  value={curso}
                  onChange={(e) => setCurso(e.target.value)}
                  className="w-full bg-[#dedede] text-gray-800 px-4 py-2.5 rounded-xl outline-none focus:ring-2 focus:ring-blue-500 text-sm appearance-none cursor-pointer pr-10"
                  required
                >
                  <option value="" disabled>
                    Selecione seu curso
                  </option>
                  <option value="ciencia-da-computacao">
                    Ciência da Computação
                  </option>
                  <option value="sistemas-de-informacao">
                    Sistemas de Informação
                  </option>
                  <option value="engenharia-de-computacao">
                    Engenharia de Computação
                  </option>
                  <option value="engenharia-mecanica">
                    Engenharia Mecânica
                  </option>
                  <option value="engenharia-eletrica">
                    Engenharia Elétrica
                  </option>
                  <option value="engenharia-civil">Engenharia Civil</option>
                  <option value="design">Design</option>
                  <option value="administracao">Administração</option>
                </select>
                <ChevronDown
                  size={18}
                  className="absolute right-3.5 top-1/2 -translate-y-1/2 text-gray-700 pointer-events-none"
                />
              </div>
            </div>

            {/* Senha */}
            <div className="flex flex-col gap-1">
              <label
                htmlFor="senha"
                className="text-xs font-semibold text-gray-800 tracking-wider"
              >
                SENHA
              </label>
              <input
                id="senha"
                type="password"
                placeholder="Mínimo de 8 caracteres"
                value={senha}
                onChange={(e) => setSenha(e.target.value)}
                autoComplete="new-password"
                className="w-full bg-[#dedede] text-gray-800 placeholder-gray-500 px-4 py-2.5 rounded-xl outline-none focus:ring-2 focus:ring-blue-500 text-sm"
                required
                minLength={8}
              />
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

            {/* Botão de Envio / Navegação */}
            <button
              type="submit"
              disabled={carregando}
              className="w-full mt-2 bg-[#2f6ff7] hover:bg-[#255cd4] disabled:opacity-60 disabled:cursor-not-allowed text-white font-semibold py-3 rounded-xl transition duration-150 shadow-sm text-sm cursor-pointer"
            >
              {carregando ? "Criando conta..." : "Continuar para Passo 2"}
            </button>
          </form>

          {/* Link para Login */}
          <div className="text-center mt-4">
            <span className="text-xs text-gray-700 font-medium">
              Já tem uma conta?{" "}
            </span>
            <Link
              href="/login"
              className="text-xs font-semibold text-[#1e2b58] hover:underline"
            >
              Fazer Login
            </Link>
          </div>
        </div>
      </main>

      {/* Espaçador inferior */}
      <footer className="h-6"></footer>
    </div>
  );
}
