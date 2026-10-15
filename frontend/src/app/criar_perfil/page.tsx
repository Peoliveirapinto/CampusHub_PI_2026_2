"use client";

import { useState, useMemo } from "react";
import Link from "next/link";
import Image from "next/image";
import { useRouter } from "next/navigation";
import { ChevronDown, X, Plus, Search, ChevronUp } from "lucide-react";

const API_URL = process.env.NEXT_PUBLIC_API_URL ?? "http://localhost:8080";

// Endpoint de criação de perfil no backend (PerfilController: @RequestMapping("/perfis"))
const ENDPOINT_PERFIL = "/perfis";

// Rota do Hub depois de salvar o perfil (mesma usada no login)
const ROTA_HUB = "/home_c/login";

// Nome, curso e id do usuário gravados no Passo 1 (criar_conta)
const CHAVE_DADOS_CADASTRO = "campushub:dadosCadastro";

// Lista padronizada para facilitar o algoritmo de matching
const LISTA_HABILIDADES_PADRAO = [
  // Linguagens & Core
  "Python",
  "Java",
  "JavaScript",
  "TypeScript",
  "C/C++",
  "C#",
  "SQL",
  // Web & Mobile
  "React",
  "Next.js",
  "React Native",
  "Node.js",
  "Spring Boot",
  "Tailwind CSS",
  "HTML/CSS",
  // Hardware, Embarcados & Nuvem
  "Arduino",
  "Raspberry Pi",
  "ESP32",
  "Docker",
  "AWS",
  "Git / GitHub",
  // Design & Produto
  "Figma",
  "UI/UX Design",
  "Photoshop / Illustrator",
  "Modelagem 3D (CAD/SolidWorks)",
  // Gestão & Métodos
  "Scrum / Kanban",
  "Gestão de Projetos",
  "Marketing & Pitch",
  "Business Intelligence",
];

const AREAS_PRINCIPAIS = [
  "Inteligência Artificial",
  "Desenvolvimento Web/Apps",
  "Automação & Hardware",
];

const AREAS_EXTRAS = [
  "Internet das Coisas (IoT)",
  "Visão Computacional & Robótica",
  "UI/UX & Design de Produto",
  "Gestão, Negócios & Inovação",
  "BIM & Construção Civil",
  "Bioengenharia & Química",
  "Cibersegurança & Redes",
  "Ciência de Dados & BI",
];

export default function CompletePerfilPage() {
  const router = useRouter();
  const [erro, setErro] = useState<string | null>(null);
  const [faltouPasso1, setFaltouPasso1] = useState(false);
  const [carregando, setCarregando] = useState(false);

  // Campos obrigatórios no banco (Tabela perfil)
  const [descricao, setDescricao] = useState("");
  const [anoIngresso, setAnoIngresso] = useState(new Date().getFullYear());
  const [semestreIngresso, setSemestreIngresso] = useState("1");
  const [contato, setContato] = useState("");
  const [disponibilidade, setDisponibilidade] = useState(
    "Período Noturno (Após 18h/ EAD)"
  );

  // Habilidades e Dropdown de seleção
  const [skills, setSkills] = useState<string[]>(["Python", "React"]);
  const [isAddingSkill, setIsAddingSkill] = useState(false);
  const [skillSearch, setSkillSearch] = useState("");

  // Áreas de interesse e expansão de 'Outros'
  const [interesses, setInteresses] = useState<string[]>([]);
  const [showOutros, setShowOutros] = useState(false);
  const [outroEspecifico, setOutroEspecifico] = useState("");

  // Filtragem de habilidades disponíveis que ainda não foram adicionadas
  const habilidadesFiltradas = useMemo(() => {
    return LISTA_HABILIDADES_PADRAO.filter(
      (item) =>
        !skills.includes(item) &&
        item.toLowerCase().includes(skillSearch.toLowerCase())
    );
  }, [skills, skillSearch]);

  const handleSelectSkill = (skill: string) => {
    if (!skills.includes(skill)) {
      setSkills([...skills, skill]);
      setSkillSearch("");
      setIsAddingSkill(false);
    }
  };

  const handleAddCustomSkill = () => {
    const trimmed = skillSearch.trim();
    if (trimmed && !skills.includes(trimmed)) {
      setSkills([...skills, trimmed]);
      setSkillSearch("");
      setIsAddingSkill(false);
    }
  };

  const handleRemoveSkill = (skillToRemove: string) => {
    setSkills(skills.filter((s) => s !== skillToRemove));
  };

  const toggleInteresse = (item: string) => {
    setInteresses((prev) =>
      prev.includes(item) ? prev.filter((i) => i !== item) : [...prev, item]
    );
  };

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    if (carregando) return;

    setErro(null);
    setFaltouPasso1(false);

    // Nome e curso vêm do Passo 1 (a tabela perfil exige os dois)
    let dadosCadastro: { nome?: string; curso?: string; usuarioId?: string } =
      {};
    try {
      dadosCadastro = JSON.parse(
        sessionStorage.getItem(CHAVE_DADOS_CADASTRO) ?? "{}"
      );
    } catch {
      // segue com objeto vazio
    }

    if (!dadosCadastro.nome || !dadosCadastro.curso || !dadosCadastro.usuarioId) {
      setFaltouPasso1(true);
      setErro(
        "Não encontramos os dados do Passo 1. Volte e preencha novamente."
      );
      return;
    }

    // Consolida áreas selecionadas + especificação se houver
    const especializacoesFinais = [...interesses];
    if (outroEspecifico.trim()) {
      especializacoesFinais.push(outroEspecifico.trim());
    }
    const especializacao = especializacoesFinais.join(", ");

    // A coluna especializacao aceita no máximo 100 caracteres
    if (especializacao.length > 100) {
      setErro(
        "As áreas de interesse ficaram longas demais. Remova algumas ou encurte o texto."
      );
      return;
    }

    // Campos conforme PerfilCriarDTO do backend.
    // Obs.: "habilidades" ainda não é enviado porque o backend não tem
    // campo/tabela para elas; o estado "skills" segue na tela para quando existir.
    const perfilPayload = {
      usuarioId: dadosCadastro.usuarioId,
      nome: dadosCadastro.nome,
      curso: dadosCadastro.curso,
      descricao: descricao.trim(),
      anoDeIngresso: Number(anoIngresso),
      semestreDeIngresso: Number(semestreIngresso),
      disponibilidade,
      contato: contato.trim(),
      especializacao: especializacao || null,
    };

    setCarregando(true);
    try {
      const resposta = await fetch(`${API_URL}${ENDPOINT_PERFIL}`, {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        credentials: "include", // envia o cookie com o JWT
        body: JSON.stringify(perfilPayload),
      });

      if (resposta.ok) {
        try {
          sessionStorage.removeItem(CHAVE_DADOS_CADASTRO);
        } catch {
          // ignora
        }
        router.replace(ROTA_HUB);
        return;
      }

      if (resposta.status === 401 || resposta.status === 403) {
        setErro("Sua sessão expirou ou não é válida. Faça login novamente.");
      } else if (resposta.status === 409) {
        setErro("Você já tem um perfil cadastrado.");
      } else if (resposta.status === 400) {
        setErro("Alguns dados do perfil estão inválidos. Confira os campos.");
      } else {
        setErro("Não foi possível salvar o perfil agora. Tente novamente.");
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

      {/* Conteúdo Central / Card Passo 2 */}
      <main className="flex-1 flex items-center justify-center px-4 py-8">
        <div className="w-full max-w-lg bg-white border border-gray-400/80 rounded-2xl p-8 shadow-sm">
          {/* Topo do Card */}
          <div className="flex justify-between items-start mb-2">
            <div>
              <h1 className="text-2xl font-bold text-gray-900">
                Complete seu Perfil
              </h1>
              <p className="text-xs text-gray-600 mt-0.5">
                Isso ajuda outros alunos a encontrarem você para trabalhos
              </p>
            </div>
            <span className="bg-[#cbe8fd] text-[#0284c7] text-xs font-semibold px-3 py-1 rounded-full whitespace-nowrap">
              Passo 2 de 2
            </span>
          </div>

          <div className="border-t border-gray-300 my-4"></div>

          {/* Formulário */}
          <form onSubmit={handleSubmit} className="flex flex-col gap-4">
            {/* MINI BIO / RESUMO */}
            <div className="flex flex-col gap-1">
              <label
                htmlFor="descricao"
                className="text-xs font-semibold text-gray-800 tracking-wider uppercase"
              >
                Mini Bio / Resumo
              </label>
              <textarea
                id="descricao"
                rows={3}
                value={descricao}
                onChange={(e) => setDescricao(e.target.value)}
                placeholder="Ex: Aluno dedicado focado em programação, banco de dados e pipeline automatizado para testes"
                className="w-full bg-[#dedede] text-gray-800 placeholder-gray-500 p-3.5 rounded-xl outline-none focus:ring-2 focus:ring-blue-500 text-sm resize-none"
                required
              />
            </div>

            {/* ANO E SEMESTRE DE INGRESSO */}
            <div className="grid grid-cols-2 gap-3">
              <div className="flex flex-col gap-1">
                <label
                  htmlFor="anoIngresso"
                  className="text-xs font-semibold text-gray-800 tracking-wider uppercase"
                >
                  Ano de Ingresso
                </label>
                <input
                  id="anoIngresso"
                  type="number"
                  min="2010"
                  max="2035"
                  value={anoIngresso}
                  onChange={(e) => setAnoIngresso(Number(e.target.value))}
                  className="w-full bg-[#dedede] text-gray-800 px-3.5 py-2.5 rounded-xl outline-none focus:ring-2 focus:ring-blue-500 text-sm"
                  required
                />
              </div>

              <div className="flex flex-col gap-1">
                <label
                  htmlFor="semestreIngresso"
                  className="text-xs font-semibold text-gray-800 tracking-wider uppercase"
                >
                  Semestre
                </label>
                <div className="relative">
                  <select
                    id="semestreIngresso"
                    value={semestreIngresso}
                    onChange={(e) => setSemestreIngresso(e.target.value)}
                    className="w-full bg-[#dedede] text-gray-800 px-3.5 py-2.5 rounded-xl outline-none focus:ring-2 focus:ring-blue-500 text-sm appearance-none cursor-pointer pr-10"
                    required
                  >
                    <option value="1">1º Semestre</option>
                    <option value="2">2º Semestre</option>
                  </select>
                  <ChevronDown
                    size={16}
                    className="absolute right-3 top-1/2 -translate-y-1/2 text-gray-600 pointer-events-none"
                  />
                </div>
              </div>
            </div>

            {/* CONTATO */}
            <div className="flex flex-col gap-1">
              <label
                htmlFor="contato"
                className="text-xs font-semibold text-gray-800 tracking-wider uppercase"
              >
                Contato (WhatsApp / Telegram / Discord)
              </label>
              <input
                id="contato"
                type="text"
                value={contato}
                onChange={(e) => setContato(e.target.value)}
                placeholder="Ex: (11) 99999-9999 ou @usuario"
                maxLength={100}
                className="w-full bg-[#dedede] text-gray-800 placeholder-gray-500 px-4 py-2.5 rounded-xl outline-none focus:ring-2 focus:ring-blue-500 text-sm"
                required
              />
            </div>

            {/* SUAS HABILIDADES (HARD SKILLS) */}
            <div className="flex flex-col gap-1 relative">
              <label className="text-xs font-semibold text-gray-800 tracking-wider uppercase">
                Suas Habilidades (Hard Skills)
              </label>

              <div className="w-full bg-[#dedede] p-2.5 rounded-xl flex flex-wrap items-center gap-2 min-h-11.5">
                {skills.map((skill) => (
                  <span
                    key={skill}
                    className="inline-flex items-center gap-1.5 bg-[#bce3fe] text-[#0284c7] px-3 py-1 rounded-full text-xs font-semibold"
                  >
                    {skill}
                    <button
                      type="button"
                      onClick={() => handleRemoveSkill(skill)}
                      className="hover:text-blue-900 transition"
                      title={`Remover ${skill}`}
                    >
                      <X size={13} />
                    </button>
                  </span>
                ))}

                <button
                  type="button"
                  onClick={() => setIsAddingSkill(!isAddingSkill)}
                  className="text-xs text-gray-700 hover:text-gray-950 inline-flex items-center gap-1 font-semibold px-2 py-1 rounded-lg hover:bg-black/5 transition cursor-pointer"
                >
                  <Plus size={13} />
                  Adicionar habilidade
                </button>
              </div>

              {/* DROPDOWN FLUTUANTE DE HABILIDADES */}
              {isAddingSkill && (
                <div className="mt-1 rounded-xl border border-gray-300 bg-white p-3 shadow-lg z-20">
                  <div className="flex items-center gap-2 border-b border-gray-200 pb-2 mb-2">
                    <Search size={14} className="text-gray-400" />
                    <input
                      type="text"
                      value={skillSearch}
                      onChange={(e) => setSkillSearch(e.target.value)}
                      onKeyDown={(e) => {
                        if (e.key === "Enter") {
                          e.preventDefault();
                          handleAddCustomSkill();
                        }
                      }}
                      placeholder="Pesquise ou digite uma habilidade..."
                      className="w-full text-xs outline-none bg-transparent placeholder-gray-400 text-gray-800"
                      autoFocus
                    />
                    {skillSearch.trim() && (
                      <button
                        type="button"
                        onClick={handleAddCustomSkill}
                        className="text-[11px] bg-blue-600 text-white px-2 py-0.5 rounded font-semibold whitespace-nowrap cursor-pointer hover:bg-blue-700"
                      >
                        + Adicionar
                      </button>
                    )}
                  </div>

                  <p className="text-[11px] font-semibold text-gray-500 mb-2">
                    Selecione da lista padronizada:
                  </p>

                  <div className="max-h-40 overflow-y-auto flex flex-wrap gap-1.5 pr-1">
                    {habilidadesFiltradas.length > 0 ? (
                      habilidadesFiltradas.map((item) => (
                        <button
                          key={item}
                          type="button"
                          onClick={() => handleSelectSkill(item)}
                          className="text-xs bg-slate-100 hover:bg-blue-100 hover:text-blue-900 text-gray-700 px-2.5 py-1 rounded-md transition text-left cursor-pointer"
                        >
                          + {item}
                        </button>
                      ))
                    ) : (
                      <span className="text-xs text-gray-400 italic">
                        Nenhuma correspondência padrão. Pressione &quot;+ Adicionar&quot; acima para incluir.
                      </span>
                    )}
                  </div>
                </div>
              )}

              <span className="text-[11px] text-gray-500 mt-0.5">
                Escolha competências pré-definidas para acelerar o match entre grupos
              </span>
            </div>

            {/* ÁREAS DE INTERESSE PARA PROJETOS */}
            <div className="flex flex-col gap-1.5 mt-1">
              <label className="text-xs font-semibold text-gray-800 tracking-wider uppercase">
                Áreas de Interesse para Projetos
              </label>

              {/* 4 Blocos Iniciais */}
              <div className="grid grid-cols-2 gap-2.5">
                {AREAS_PRINCIPAIS.map((area) => {
                  const selected = interesses.includes(area);
                  return (
                    <button
                      type="button"
                      key={area}
                      onClick={() => toggleInteresse(area)}
                      className={`flex items-center gap-2.5 p-3 rounded-xl border text-left text-xs font-medium transition cursor-pointer ${
                        selected
                          ? "bg-white border-blue-600 text-blue-950 shadow-xs"
                          : "bg-[#dedede] border-transparent text-gray-800 hover:bg-[#d5d5d5]"
                      }`}
                    >
                      <div
                        className={`h-4 w-4 rounded flex items-center justify-center border transition ${
                          selected
                            ? "bg-blue-600 border-blue-600 text-white"
                            : "bg-white border-gray-400"
                        }`}
                      >
                        {selected && <div className="h-2 w-2 bg-white rounded-xs" />}
                      </div>
                      <span>{area}</span>
                    </button>
                  );
                })}

                {/* Botão Outros */}
                <button
                  type="button"
                  onClick={() => setShowOutros(!showOutros)}
                  className={`flex items-center justify-between p-3 rounded-xl border text-left text-xs font-semibold transition cursor-pointer ${
                    showOutros || interesses.some((i) => AREAS_EXTRAS.includes(i))
                      ? "bg-white border-blue-600 text-blue-950"
                      : "bg-[#dedede] border-transparent text-gray-800 hover:bg-[#d5d5d5]"
                  }`}
                >
                  <span>Outros</span>
                  {showOutros ? <ChevronUp size={16} /> : <ChevronDown size={16} />}
                </button>
              </div>

              {/* LISTA EXPANDIDA AO CLICAR EM OUTROS */}
              {showOutros && (
                <div className="mt-2 rounded-xl border border-blue-200 bg-blue-50/50 p-3.5 flex flex-col gap-2.5 animate-in fade-in duration-200">
                  <span className="text-[11px] font-bold uppercase tracking-wider text-blue-950">
                    Selecione áreas complementares:
                  </span>

                  <div className="grid grid-cols-1 sm:grid-cols-2 gap-2">
                    {AREAS_EXTRAS.map((extraArea) => {
                      const selected = interesses.includes(extraArea);
                      return (
                        <button
                          type="button"
                          key={extraArea}
                          onClick={() => toggleInteresse(extraArea)}
                          className={`flex items-center gap-2 p-2 rounded-lg text-left text-xs font-medium border transition cursor-pointer ${
                            selected
                              ? "bg-white border-blue-600 text-blue-950 shadow-2xs"
                              : "bg-white/80 border-gray-200 text-gray-700 hover:bg-white"
                          }`}
                        >
                          <div
                            className={`h-3.5 w-3.5 rounded flex items-center justify-center border transition ${
                              selected
                                ? "bg-blue-600 border-blue-600"
                                : "border-gray-400 bg-white"
                            }`}
                          >
                            {selected && <div className="h-1.5 w-1.5 bg-white rounded-xs" />}
                          </div>
                          <span className="truncate">{extraArea}</span>
                        </button>
                      );
                    })}
                  </div>

                  {/* Campo de texto livre adicional */}
                  <div className="mt-1 pt-2 border-t border-blue-200/60">
                    <label className="text-[11px] font-semibold text-gray-700">
                      Não encontrou sua área? Especifique:
                    </label>
                    <input
                      type="text"
                      value={outroEspecifico}
                      onChange={(e) => setOutroEspecifico(e.target.value)}
                      placeholder="Ex: Energias Renováveis, Nanotecnologia..."
                      maxLength={60}
                      className="mt-1 w-full bg-white border border-gray-300 rounded-lg px-3 py-1.5 text-xs outline-none focus:ring-1 focus:ring-blue-500"
                    />
                  </div>
                </div>
              )}
            </div>

            {/* DISPONIBILIDADE PRINCIPAL */}
            <div className="flex flex-col gap-1 mt-1">
              <label
                htmlFor="disponibilidade"
                className="text-xs font-semibold text-gray-800 tracking-wider uppercase"
              >
                Disponibilidade Principal
              </label>
              <div className="relative">
                <select
                  id="disponibilidade"
                  value={disponibilidade}
                  onChange={(e) => setDisponibilidade(e.target.value)}
                  className="w-full bg-[#dedede] text-gray-800 px-4 py-2.5 rounded-xl outline-none focus:ring-2 focus:ring-blue-500 text-sm appearance-none cursor-pointer pr-10"
                  required
                >
                  <option value="Período Noturno (Após 18h/ EAD)">
                    Período Noturno (Após 18h/ EAD)
                  </option>
                  <option value="Período Matutino (Manhã)">
                    Período Matutino (Manhã)
                  </option>
                  <option value="Período Vespertino (Tarde)">
                    Período Vespertino (Tarde)
                  </option>
                  <option value="Finais de Semana e Horários Livres">
                    Finais de Semana e Horários Livres
                  </option>
                </select>
                <ChevronDown
                  size={18}
                  className="absolute right-3.5 top-1/2 -translate-y-1/2 text-gray-700 pointer-events-none"
                />
              </div>
            </div>

            {/* Mensagem de erro */}
            {erro && (
              <div
                role="alert"
                className="text-sm font-medium text-red-700 bg-red-50 border border-red-200 rounded-lg px-3 py-2"
              >
                <p>{erro}</p>
                {faltouPasso1 && (
                  <Link
                    href="/criar_conta"
                    className="inline-block mt-1 font-semibold underline"
                  >
                    Voltar ao Passo 1
                  </Link>
                )}
              </div>
            )}

            {/* Botão Salvar Perfil */}
            <button
              type="submit"
              disabled={carregando}
              className="w-full mt-3 bg-[#2f6ff7] hover:bg-[#255cd4] disabled:opacity-60 disabled:cursor-not-allowed text-white font-semibold py-3 rounded-xl transition duration-150 shadow-sm text-sm cursor-pointer"
            >
              {carregando ? "Salvando..." : "Salvar Perfil e Acessar o Hub"}
            </button>
          </form>

          {/* Opção Pular */}
          <div className="text-center mt-3">
            <Link
              href="/"
              className="text-xs text-gray-600 hover:text-gray-900 transition hover:underline"
            >
              Pular esta etapa por enquanto
            </Link>
          </div>
        </div>
      </main>

      {/* Espaçador inferior */}
      <footer className="h-6"></footer>
    </div>
  );
}