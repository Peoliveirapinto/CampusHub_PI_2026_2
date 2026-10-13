"use client";

import React, { useState, useMemo, useRef, useEffect } from "react";
import Link from "next/link";
import Image from "next/image";
import { useRouter } from "next/navigation";
import {
  ArrowLeft,
  User,
  ChevronDown,
  X,
  Plus,
  Search,
  Check,
} from "lucide-react";

// Lista padronizada para busca rápida de habilidades
const LISTA_HABILIDADES_PADRAO = [
  "Python",
  "React",
  "Java",
  "JavaScript",
  "TypeScript",
  "C/C++",
  "C#",
  "SQL",
  "Next.js",
  "Node.js",
  "Spring Boot",
  "Arduino",
  "Raspberry Pi",
  "ESP32",
  "Docker",
  "AWS",
  "Git / GitHub",
  "Figma",
  "UI/UX Design",
  "Modelagem 3D (CAD/SolidWorks)",
  "Scrum / Kanban",
  "Gestão de Projetos",
  "Marketing & Pitch",
];

const OPCOES_DISPONIBILIDADE = [
  "Período Noturno (Após 18h/ EAD)",
  "Período Matutino (Manhã)",
  "Período Vespertino (Tarde)",
  "Finais de Semana e Horários Livres",
];

export default function EditarPerfilPage() {
  const router = useRouter();

  // Estados com dados iniciais de exemplo (mock data)
  const [bio, setBio] = useState<string>(
    "Aluno dedicado focado em programação, banco de dados e pipeline automatizado para testes"
  );
  const [skills, setSkills] = useState<string[]>(["Python", "React"]);
  const [interesses, setInteresses] = useState<string[]>([
    "Inteligência Artificial",
  ]);
  const [disponibilidade, setDisponibilidade] = useState<string>(
    "Período Noturno (Após 18h/ EAD)"
  );

  // Controle da janelinha flutuante para adicionar habilidades
  const [isAddingSkill, setIsAddingSkill] = useState<boolean>(false);
  const [skillSearch, setSkillSearch] = useState<string>("");
  const popoverSkillRef = useRef<HTMLDivElement>(null);

  // Fecha o dropdown flutuante ao clicar fora
  useEffect(() => {
    function handleClickFora(event: MouseEvent) {
      if (
        popoverSkillRef.current &&
        !popoverSkillRef.current.contains(event.target as Node)
      ) {
        setIsAddingSkill(false);
      }
    }
    document.addEventListener("mousedown", handleClickFora);
    return () => document.removeEventListener("mousedown", handleClickFora);
  }, []);

  // Filtragem de habilidades
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

  const toggleInteresse = (area: string) => {
    setInteresses((prev) =>
      prev.includes(area) ? prev.filter((a) => a !== area) : [...prev, area]
    );
  };

  // Simulação de salvar alterações
  const handleSubmit = (e: React.FormEvent) => {
    e.preventDefault();

    const dadosAtualizados = {
      bio,
      skills,
      interesses,
      disponibilidade,
    };

    console.log("Perfil atualizado (Mock):", dadosAtualizados);
    if (typeof window !== "undefined") {
      localStorage.setItem("campushub_perfil", JSON.stringify(dadosAtualizados));
    }

    alert("Alterações salvas com sucesso!");
    router.push("/");
  };

  return (
    <div className="min-h-screen bg-[#F0F4F8] flex flex-col font-sans text-slate-800">
      {/* 1. Cabeçalho Superior Institucional */}
      <header className="w-full px-6 sm:px-10 py-5 flex items-center justify-between">
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
        <span className="text-sm font-medium text-slate-900">
          Instituto Mauá de Tecnologia
        </span>
      </header>

      {/* 2. Conteúdo Central / Card de Edição */}
      <main className="flex-1 flex items-center justify-center p-4 pb-14">
        <div className="w-full max-w-2xl bg-white rounded-3xl border border-slate-300 shadow-xs p-7 sm:p-10 relative">
          {/* Topo do Formulário */}
          <div className="flex justify-between items-start pb-4 border-b border-slate-200">
            <div>
              <h1 className="text-2xl font-bold text-slate-950">
                Editar Perfil
              </h1>
              <p className="text-xs text-slate-600 mt-1">
                Mantenha suas competências atualizadas para melhores recomendações
              </p>
            </div>

            {/* Botão Pílula "Voltar ao hub" */}
            <Link
              href="/"
              className="inline-flex items-center gap-1.5 px-4 py-1.5 rounded-full border border-sky-400 bg-sky-50 text-sky-700 hover:bg-sky-100 text-xs font-semibold transition shadow-2xs"
            >
              <ArrowLeft size={13} />
              Voltar ao hub
            </Link>
          </div>

          <form onSubmit={handleSubmit} className="mt-5 space-y-5">
            {/* Bloco de Apresentação do Usuário */}
            <div className="w-full bg-[#E5E7EB]/70 rounded-2xl p-4 flex items-center gap-4">
              <div className="w-16 h-16 rounded-full bg-[#3B82F6] flex items-center justify-center text-white shrink-0 shadow-xs">
                <User size={38} strokeWidth={2.2} />
              </div>
              <div>
                <h2 className="text-lg font-bold text-slate-900">Mario Bros</h2>
                <p className="text-sm text-slate-600 font-normal">
                  Engenharia - IMT
                </p>
              </div>
            </div>

            {/* Mini Bio / Resumo */}
            <div>
              <label
                htmlFor="bio"
                className="block text-xs font-bold text-slate-800 tracking-wide mb-1.5 uppercase"
              >
                Mini Bio/ Resumo
              </label>
              <textarea
                id="bio"
                rows={3}
                value={bio}
                onChange={(e) => setBio(e.target.value)}
                placeholder="Ex: Aluno dedicado focado em programação, banco de dados e pipeline automatizado para testes"
                className="w-full bg-[#E5E7EB]/80 text-slate-800 placeholder-slate-500 rounded-2xl p-4 text-xs sm:text-sm outline-none border border-transparent focus:border-slate-400 focus:bg-white transition resize-none leading-relaxed"
              />
            </div>

            {/* Suas Habilidades (Hard Skills) */}
            <div>
              <label className="block text-xs font-bold text-slate-800 tracking-wide mb-1.5 uppercase">
                Suas Habilidades (Hard Skills)
              </label>
              <div className="w-full bg-[#E5E7EB]/80 rounded-2xl px-4 py-3 flex flex-wrap items-center gap-2">
                {skills.map((tag) => (
                  <span
                    key={tag}
                    className="inline-flex items-center gap-1.5 bg-[#93C5FD] text-slate-950 font-medium text-xs px-3 py-1.5 rounded-xl shadow-xs"
                  >
                    {tag}
                    <button
                      type="button"
                      onClick={() => handleRemoveSkill(tag)}
                      className="hover:opacity-60 transition"
                      title={`Remover ${tag}`}
                    >
                      <X size={13} strokeWidth={2.5} />
                    </button>
                  </span>
                ))}

                {/* Popover flutuante para adicionar tag */}
                <div className="relative inline-block" ref={popoverSkillRef}>
                  <button
                    type="button"
                    onClick={() => setIsAddingSkill(!isAddingSkill)}
                    className="inline-flex items-center gap-1 text-xs text-slate-600 hover:text-slate-950 font-medium px-2 py-1 transition-colors rounded-lg hover:bg-slate-300/60 cursor-pointer"
                  >
                    <Plus size={13} /> Adicionar habilidade
                  </button>

                  {isAddingSkill && (
                    <div className="absolute left-0 mt-2 w-72 bg-white rounded-2xl shadow-xl border border-slate-200 p-3 z-30 animate-in fade-in zoom-in-95 duration-150">
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
                          placeholder="Pesquise ou digite..."
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

                      <p className="text-[11px] font-semibold text-gray-500 mb-2 px-1">
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
                          <span className="text-xs text-gray-400 italic px-1">
                            Pressione &quot;+ Adicionar&quot; para incluir &quot;{skillSearch}&quot;
                          </span>
                        )}
                      </div>
                    </div>
                  )}
                </div>
              </div>
              <p className="text-[11px] text-slate-500 mt-1.5">
                Digite ferramentas, linguagens ou competências (ex: Scrum, Figma, Marketing)
              </p>
            </div>

            {/* Áreas de Interesse para Projetos */}
            <div>
              <label className="block text-xs font-bold text-slate-800 tracking-wide mb-1.5 uppercase">
                Áreas de Interesse para Projetos
              </label>
              <div className="grid grid-cols-1 sm:grid-cols-2 gap-2.5">
                {[
                  "Inteligência Artificial",
                  "Desenvolvimento Web/Apps",
                  "Automação & Hardware",
                  "Outros",
                ].map((area) => {
                  const isChecked = interesses.includes(area);
                  return (
                    <button
                      key={area}
                      type="button"
                      onClick={() => toggleInteresse(area)}
                      className="w-full bg-[#E5E7EB]/80 hover:bg-[#E5E7EB] transition-colors rounded-xl px-4 py-3 flex items-center gap-3 text-left cursor-pointer"
                    >
                      <div
                        className={`w-5 h-5 rounded-md border flex items-center justify-center transition ${
                          isChecked
                            ? "bg-blue-600 border-blue-600 text-white"
                            : "bg-white border-slate-400"
                        }`}
                      >
                        {isChecked && <Check size={14} strokeWidth={3} />}
                      </div>
                      <span className="text-xs sm:text-sm font-medium text-slate-800">
                        {area}
                      </span>
                    </button>
                  );
                })}
              </div>
            </div>

            {/* Disponibilidade Principal */}
            <div>
              <label
                htmlFor="disponibilidade"
                className="block text-xs font-bold text-slate-800 tracking-wide mb-1.5 uppercase"
              >
                Disponibilidade Principal
              </label>
              <div className="relative">
                <select
                  id="disponibilidade"
                  value={disponibilidade}
                  onChange={(e) => setDisponibilidade(e.target.value)}
                  className="w-full appearance-none bg-[#E5E7EB]/80 text-slate-800 rounded-2xl px-5 py-3.5 text-xs sm:text-sm outline-none border border-transparent focus:border-slate-400 focus:bg-white transition cursor-pointer pr-10"
                >
                  {OPCOES_DISPONIBILIDADE.map((opcao) => (
                    <option key={opcao} value={opcao}>
                      {opcao}
                    </option>
                  ))}
                </select>
                <ChevronDown
                  size={18}
                  className="absolute right-4 top-1/2 -translate-y-1/2 text-slate-500 pointer-events-none"
                />
              </div>
            </div>

            {/* Botão de Envio (Salvar Alterações) */}
            <div className="pt-2 flex justify-center">
              <button
                type="submit"
                className="w-full max-w-sm py-3.5 bg-[#2563EB] hover:bg-blue-700 text-white font-bold text-sm rounded-full transition shadow-md hover:shadow-lg active:scale-[0.99] cursor-pointer"
              >
                Salvar Alterações
              </button>
            </div>
          </form>

          {/* Links Inferiores */}
          <div className="mt-6 pt-4 flex items-center justify-between text-xs font-medium border-t border-slate-100">
            <Link
              href="/recuperar-senha"
              className="text-blue-600 hover:text-blue-800 hover:underline transition-colors"
            >
              Alterar senha de acesso
            </Link>
            <button
              type="button"
              onClick={() => {
                if (window.confirm("Deseja realmente sair da conta?")) {
                  router.push("/login");
                }
              }}
              className="text-red-500 hover:text-red-700 hover:underline transition-colors cursor-pointer"
            >
              Sair da conta
            </button>
          </div>
        </div>
      </main>
    </div>
  );
}