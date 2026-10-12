"use client";

import React, { useState, useMemo, useRef, useEffect } from "react";
import { ChevronDown, X, Plus, Search } from "lucide-react";
import Link from "next/link";
import Image from "next/image";
import { useRouter } from "next/navigation";

// Mesma lista padronizada da tela de perfil
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

// Mesmas opções de disponibilidade da tela de perfil
const OPCOES_DISPONIBILIDADE = [
  "Período Noturno (Após 18h/ EAD)",
  "Período Matutino (Manhã)",
  "Período Vespertino (Tarde)",
  "Finais de Semana e Horários Livres",
];

export default function CriarGrupoPage() {
  const router = useRouter();

  // Estados dos campos de texto
  const [nomeProjeto, setNomeProjeto] = useState<string>("");
  const [disciplina, setDisciplina] = useState<string>(
    "Projeto Integrador Interdisciplinar (PI)",
  );
  const [maxIntegrantes, setMaxIntegrantes] = useState<string>(
    "4 Integrantes (Padrão PI)",
  );
  const [disponibilidade, setDisponibilidade] = useState<string>(
    OPCOES_DISPONIBILIDADE[0],
  );
  const [descricao, setDescricao] = useState<string>("");

  // Tags selecionadas
  const [competenciasPossui, setCompetenciasPossui] = useState<string[]>([
    "Python",
    "React",
  ]);
  const [competenciasProcura, setCompetenciasProcura] = useState<string[]>([
    "Python",
    "React",
  ]);

  // Controles dos Popovers/Dropdowns Flutuantes
  const [isAddingPossui, setIsAddingPossui] = useState<boolean>(false);
  const [buscaPossui, setBuscaPossui] = useState<string>("");

  const [isAddingProcura, setIsAddingProcura] = useState<boolean>(false);
  const [buscaProcura, setBuscaProcura] = useState<string>("");

  const popoverPossuiRef = useRef<HTMLDivElement>(null);
  const popoverProcuraRef = useRef<HTMLDivElement>(null);

  // Fecha menus ao clicar fora
  useEffect(() => {
    function handleClickFora(event: MouseEvent) {
      if (
        popoverPossuiRef.current &&
        !popoverPossuiRef.current.contains(event.target as Node)
      ) {
        setIsAddingPossui(false);
      }
      if (
        popoverProcuraRef.current &&
        !popoverProcuraRef.current.contains(event.target as Node)
      ) {
        setIsAddingProcura(false);
      }
    }
    document.addEventListener("mousedown", handleClickFora);
    return () => document.removeEventListener("mousedown", handleClickFora);
  }, []);

  // Filtros de habilidades padrão (exclui o que já foi adicionado)
  const habilidadesFiltradasPossui = useMemo(() => {
    return LISTA_HABILIDADES_PADRAO.filter(
      (item) =>
        !competenciasPossui.includes(item) &&
        item.toLowerCase().includes(buscaPossui.toLowerCase()),
    );
  }, [competenciasPossui, buscaPossui]);

  const habilidadesFiltradasProcura = useMemo(() => {
    return LISTA_HABILIDADES_PADRAO.filter(
      (item) =>
        !competenciasProcura.includes(item) &&
        item.toLowerCase().includes(buscaProcura.toLowerCase()),
    );
  }, [competenciasProcura, buscaProcura]);

  // Handlers - Competências que POSSUI
  const handleSelectPossui = (skill: string) => {
    if (!competenciasPossui.includes(skill)) {
      setCompetenciasPossui([...competenciasPossui, skill]);
      setBuscaPossui("");
      setIsAddingPossui(false);
    }
  };

  const handleAddCustomPossui = () => {
    const trimmed = buscaPossui.trim();
    if (trimmed && !competenciasPossui.includes(trimmed)) {
      setCompetenciasPossui([...competenciasPossui, trimmed]);
      setBuscaPossui("");
      setIsAddingPossui(false);
    }
  };

  const handleRemovePossui = (skillToRemove: string) => {
    setCompetenciasPossui(
      competenciasPossui.filter((s) => s !== skillToRemove),
    );
  };

  // Handlers - Competências que PROCURA (Vagas)
  const handleSelectProcura = (skill: string) => {
    if (!competenciasProcura.includes(skill)) {
      setCompetenciasProcura([...competenciasProcura, skill]);
      setBuscaProcura("");
      setIsAddingProcura(false);
    }
  };

  const handleAddCustomProcura = () => {
    const trimmed = buscaProcura.trim();
    if (trimmed && !competenciasProcura.includes(trimmed)) {
      setCompetenciasProcura([...competenciasProcura, trimmed]);
      setBuscaProcura("");
      setIsAddingProcura(false);
    }
  };

  const handleRemoveProcura = (skillToRemove: string) => {
    setCompetenciasProcura(
      competenciasProcura.filter((s) => s !== skillToRemove),
    );
  };

  const handleSubmit = (e: React.FormEvent) => {
    e.preventDefault();
    if (!nomeProjeto.trim()) {
      alert("Por favor, preencha o nome do projeto ou tema.");
      return;
    }

    const payload = {
      nomeProjeto,
      disciplina,
      maxIntegrantes,
      competenciasPossui,
      competenciasProcura,
      disponibilidade,
      descricao,
    };

    console.log("Grupo Criado:", payload);
    alert(`Grupo "${nomeProjeto}" publicado com sucesso!`);
    router.push("/");
  };

  return (
    <div className="min-h-screen bg-[#F0F4F8] flex flex-col font-sans text-slate-800">
      {/* 1. Topo / Header Institucional */}
      <header className="w-full px-6 sm:px-10 py-5 flex items-center justify-between">
        <Link
          href="/"
          className="flex items-center group transition-opacity hover:opacity-90"
        >
          <Image
            src="/logo_CampusHub_transparente_e_recortado (1).png"
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

      {/* 2. Container Central */}
      <main className="flex-1 flex items-center justify-center p-4 pb-16">
        <div className="w-full max-w-2xl bg-white rounded-3xl border border-slate-300 shadow-sm p-7 sm:p-10 relative">
          {/* Cabeçalho do Card */}
          <div className="flex justify-between items-start pb-4 border-b border-slate-200">
            <div>
              <h1 className="text-2xl font-bold text-slate-950">
                Criar Grupo de Projeto
              </h1>
              <p className="text-xs text-slate-600 mt-1">
                Abra vagas e encontre integrantes com habilidades
                complementares.
              </p>
            </div>
            <button
              type="button"
              onClick={() => router.back()}
              className="text-sm text-slate-600 hover:text-slate-950 font-medium transition-colors cursor-pointer"
            >
              Cancelar
            </button>
          </div>

          <form onSubmit={handleSubmit} className="mt-6 space-y-5">
            {/* Nome do Projeto */}
            <div>
              <label className="block text-xs font-bold text-slate-800 tracking-wide mb-2 uppercase">
                Nome do Projeto ou Tema
              </label>
              <input
                type="text"
                value={nomeProjeto}
                onChange={(e) => setNomeProjeto(e.target.value)}
                placeholder="Ex: CampusHub - Plataforma Acadêmica"
                className="w-full bg-[#E5E7EB]/80 text-slate-800 placeholder-slate-500 rounded-2xl px-5 py-3.5 text-sm outline-none border border-transparent focus:border-slate-400 focus:bg-white transition"
                required
              />
            </div>

            {/* Disciplina */}
            <div>
              <label className="block text-xs font-bold text-slate-800 tracking-wide mb-2 uppercase">
                Disciplina / Contexto Acadêmico
              </label>
              <input
                type="text"
                value={disciplina}
                onChange={(e) => setDisciplina(e.target.value)}
                placeholder="Projeto Integrador Interdisciplinar (PI)"
                className="w-full bg-[#E5E7EB]/80 text-slate-800 placeholder-slate-500 rounded-2xl px-5 py-3.5 text-sm outline-none border border-transparent focus:border-slate-400 focus:bg-white transition"
              />
            </div>

            {/* Número Máximo de Integrantes */}
            <div>
              <label className="block text-xs font-bold text-slate-800 tracking-wide mb-2 uppercase">
                Número Máximo de Integrantes
              </label>
              <div className="relative">
                <select
                  value={maxIntegrantes}
                  onChange={(e) => setMaxIntegrantes(e.target.value)}
                  className="w-full appearance-none bg-[#E5E7EB]/80 text-slate-800 rounded-2xl px-5 py-3.5 text-sm outline-none border border-transparent focus:border-slate-400 focus:bg-white transition cursor-pointer pr-10"
                >
                  <option value="3 Integrantes">3 Integrantes</option>
                  <option value="4 Integrantes (Padrão PI)">
                    4 Integrantes (Padrão PI)
                  </option>
                  <option value="5 Integrantes">5 Integrantes</option>
                  <option value="6 Integrantes">6 Integrantes</option>
                </select>
                <ChevronDown
                  size={18}
                  className="absolute right-4 top-1/2 -translate-y-1/2 text-slate-500 pointer-events-none"
                />
              </div>
            </div>

            {/* 1. Competências que o grupo já possui */}
            <div className="relative">
              <label className="block text-xs font-bold text-slate-800 tracking-wide mb-2 uppercase">
                Competências que o grupo já possui
              </label>
              <div className="w-full bg-[#E5E7EB]/80 rounded-2xl px-4 py-3 flex flex-wrap items-center gap-2">
                {competenciasPossui.map((tag) => (
                  <span
                    key={tag}
                    className="inline-flex items-center gap-1.5 bg-[#93C5FD] text-slate-950 font-medium text-xs px-3 py-1.5 rounded-xl shadow-xs"
                  >
                    {tag}
                    <button
                      type="button"
                      onClick={() => handleRemovePossui(tag)}
                      className="hover:opacity-60 transition"
                      title={`Remover ${tag}`}
                    >
                      <X size={13} strokeWidth={2.5} />
                    </button>
                  </span>
                ))}

                {/* Popover de adição */}
                <div className="relative inline-block" ref={popoverPossuiRef}>
                  <button
                    type="button"
                    onClick={() => setIsAddingPossui(!isAddingPossui)}
                    className="inline-flex items-center gap-1 text-xs text-slate-600 hover:text-slate-950 font-medium px-2 py-1 transition-colors rounded-lg hover:bg-slate-300/60 cursor-pointer"
                  >
                    <Plus size={13} /> Adicionar habilidade
                  </button>

                  {isAddingPossui && (
                    <div className="absolute left-0 mt-2 w-72 bg-white rounded-2xl shadow-xl border border-slate-200 p-3 z-30 animate-in fade-in zoom-in-95 duration-150">
                      <div className="flex items-center gap-2 border-b border-gray-200 pb-2 mb-2">
                        <Search size={14} className="text-gray-400" />
                        <input
                          type="text"
                          value={buscaPossui}
                          onChange={(e) => setBuscaPossui(e.target.value)}
                          onKeyDown={(e) => {
                            if (e.key === "Enter") {
                              e.preventDefault();
                              handleAddCustomPossui();
                            }
                          }}
                          placeholder="Pesquise ou digite..."
                          className="w-full text-xs outline-none bg-transparent placeholder-gray-400 text-gray-800"
                          autoFocus
                        />
                        {buscaPossui.trim() && (
                          <button
                            type="button"
                            onClick={handleAddCustomPossui}
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
                        {habilidadesFiltradasPossui.length > 0 ? (
                          habilidadesFiltradasPossui.map((item) => (
                            <button
                              key={item}
                              type="button"
                              onClick={() => handleSelectPossui(item)}
                              className="text-xs bg-slate-100 hover:bg-blue-100 hover:text-blue-900 text-gray-700 px-2.5 py-1 rounded-md transition text-left cursor-pointer"
                            >
                              + {item}
                            </button>
                          ))
                        ) : (
                          <span className="text-xs text-gray-400 italic px-1">
                            Pressione &quot;+ Adicionar&quot; para incluir
                            &quot;{buscaPossui}&quot;
                          </span>
                        )}
                      </div>
                    </div>
                  )}
                </div>
              </div>
            </div>

            {/* 2. Competências que o grupo está procurando (Vagas) */}
            <div className="relative">
              <label className="block text-xs font-bold text-[#F87171] tracking-wide mb-2 uppercase">
                Competências que o grupo está procurando (Vagas)
              </label>
              <div className="w-full bg-[#FCA5A5]/45 rounded-2xl px-4 py-3 flex flex-wrap items-center gap-2">
                {competenciasProcura.map((tag) => (
                  <span
                    key={tag}
                    className="inline-flex items-center gap-1.5 bg-[#F87171] text-slate-950 font-medium text-xs px-3 py-1.5 rounded-xl shadow-xs"
                  >
                    {tag}
                    <button
                      type="button"
                      onClick={() => handleRemoveProcura(tag)}
                      className="hover:opacity-60 transition"
                      title={`Remover ${tag}`}
                    >
                      <X size={13} strokeWidth={2.5} />
                    </button>
                  </span>
                ))}

                {/* Popover de adição */}
                <div className="relative inline-block" ref={popoverProcuraRef}>
                  <button
                    type="button"
                    onClick={() => setIsAddingProcura(!isAddingProcura)}
                    className="inline-flex items-center gap-1 text-xs text-slate-700 hover:text-slate-950 font-medium px-2 py-1 transition-colors rounded-lg hover:bg-red-200/50 cursor-pointer"
                  >
                    <Plus size={13} /> Adicionar habilidade
                  </button>

                  {isAddingProcura && (
                    <div className="absolute left-0 mt-2 w-72 bg-white rounded-2xl shadow-xl border border-red-200 p-3 z-30 animate-in fade-in zoom-in-95 duration-150">
                      <div className="flex items-center gap-2 border-b border-red-200 pb-2 mb-2">
                        <Search size={14} className="text-gray-400" />
                        <input
                          type="text"
                          value={buscaProcura}
                          onChange={(e) => setBuscaProcura(e.target.value)}
                          onKeyDown={(e) => {
                            if (e.key === "Enter") {
                              e.preventDefault();
                              handleAddCustomProcura();
                            }
                          }}
                          placeholder="Pesquise ou digite..."
                          className="w-full text-xs outline-none bg-transparent placeholder-gray-400 text-gray-800"
                          autoFocus
                        />
                        {buscaProcura.trim() && (
                          <button
                            type="button"
                            onClick={handleAddCustomProcura}
                            className="text-[11px] bg-red-600 text-white px-2 py-0.5 rounded font-semibold whitespace-nowrap cursor-pointer hover:bg-red-700"
                          >
                            + Adicionar
                          </button>
                        )}
                      </div>

                      <p className="text-[11px] font-semibold text-red-500 mb-2 px-1">
                        Selecione da lista padronizada:
                      </p>

                      <div className="max-h-40 overflow-y-auto flex flex-wrap gap-1.5 pr-1">
                        {habilidadesFiltradasProcura.length > 0 ? (
                          habilidadesFiltradasProcura.map((item) => (
                            <button
                              key={item}
                              type="button"
                              onClick={() => handleSelectProcura(item)}
                              className="text-xs bg-slate-100 hover:bg-red-100 hover:text-red-950 text-gray-700 px-2.5 py-1 rounded-md transition text-left cursor-pointer"
                            >
                              + {item}
                            </button>
                          ))
                        ) : (
                          <span className="text-xs text-gray-400 italic px-1">
                            Pressione &quot;+ Adicionar&quot; para incluir
                            &quot;{buscaProcura}&quot;
                          </span>
                        )}
                      </div>
                    </div>
                  )}
                </div>
              </div>
            </div>

            {/* 3. Disponibilidade Principal */}
            <div>
              <label
                htmlFor="disponibilidade"
                className="block text-xs font-bold text-slate-800 tracking-wide mb-2 uppercase"
              >
                Disponibilidade Principal
              </label>
              <div className="relative">
                <select
                  id="disponibilidade"
                  value={disponibilidade}
                  onChange={(e) => setDisponibilidade(e.target.value)}
                  className="w-full bg-[#E5E7EB]/80 text-slate-800 rounded-2xl px-5 py-3.5 text-sm outline-none border border-transparent focus:border-slate-400 focus:bg-white transition cursor-pointer appearance-none pr-10"
                  required
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

            {/* Descrição Curta do Projeto */}
            <div>
              <label className="block text-xs font-bold text-slate-800 tracking-wide mb-2 uppercase">
                Descrição Curta do Projeto
              </label>
              <textarea
                rows={3}
                value={descricao}
                onChange={(e) => setDescricao(e.target.value)}
                placeholder="Explique brevemente o escopo do projeto para atrair os colegas certos..."
                className="w-full bg-[#E5E7EB]/80 text-slate-800 placeholder-slate-500 rounded-2xl p-5 text-sm outline-none border border-transparent focus:border-slate-400 focus:bg-white transition resize-none"
              />
            </div>

            {/* Botão de Envio */}
            <div className="pt-3 flex justify-center">
              <button
                type="submit"
                className="w-full max-w-sm py-3.5 bg-[#2563EB] hover:bg-blue-700 text-white font-bold text-sm rounded-full transition shadow-md hover:shadow-lg active:scale-[0.99] cursor-pointer"
              >
                Publicar Grupo e Abrir Vagas
              </button>
            </div>
          </form>
        </div>
      </main>
    </div>
  );
}
