import Link from "next/link";
import Image from "next/image";
import {
  LogIn,
  Users,
  Boxes,
  Network,
  ArrowRight,
  CheckCircle2,
  Sparkles,
  ShieldCheck,
  Compass,
  GraduationCap,
  Layers,
  HelpCircle,
} from "lucide-react";

export default function Home() {
  return (
    <main className="min-h-screen bg-slate-50 text-slate-800 font-sans antialiased selection:bg-lime-400 selection:text-slate-950">
      {/* 1. NAVBAR / CABEÇALHO */}
      <nav className="fixed top-0 z-50 w-full border-b border-gray-200 bg-white/85 backdrop-blur-md">
        <div className="mx-auto flex h-16 max-w-7xl items-center px-4 sm:px-6 lg:px-8">
          <div className="flex flex-1 items-center justify-start">
            <Link href="/" className="flex items-center">
              <Image
                src="/logo_CampusHub1.jpg"
                alt="Logo CampusHub"
                width={160}
                height={40}
                className="h-9 w-auto object-contain"
                priority
              />
            </Link>
          </div>

          <div className="hidden md:flex items-center justify-center gap-8 text-sm font-medium text-gray-600">
            <a href="#proposito" className="transition hover:text-blue-900">
              O Problema
            </a>
            <a href="#recursos" className="transition hover:text-blue-900">
              Recursos
            </a>
            <a href="#como-funciona" className="transition hover:text-blue-900">
              Como Funciona
            </a>
            <a href="#cursos" className="transition hover:text-blue-900">
              Cursos
            </a>
            <a href="#faq" className="transition hover:text-blue-900">
              Dúvidas
            </a>
          </div>

          <div className="flex flex-1 items-center justify-end">
            <Link
              href="/login"
              className="inline-flex items-center gap-2 rounded-lg bg-blue-950 px-4 py-2 text-sm font-semibold text-white transition-all hover:bg-blue-900 hover:shadow-sm"
            >

              <LogIn size={15} />Entrar
            </Link>
          </div>
        </div>
      </nav>

      {/* 2. HERO */}
      <section className="relative overflow-hidden border-b border-slate-200 bg-white pt-32">
        <div className="pointer-events-none absolute inset-0 overflow-hidden">
          <div className="absolute -right-32 top-20 h-96 w-96 rounded-full bg-blue-900/4" />
          <div className="absolute -left-40 bottom-0 h-96 w-96 rounded-full bg-lime-400/10" />
          <div
            className="absolute inset-0 opacity-[0.035]"
            style={{
              backgroundImage:
                "linear-gradient(#0f172a 1px, transparent 1px), linear-gradient(90deg, #0f172a 1px, transparent 1px)",
              backgroundSize: "48px 48px",
            }}
          />
        </div>

        <div className="relative mx-auto max-w-7xl px-5 pb-24 sm:px-8">
          <div className="mx-auto max-w-4xl text-center">
            <h1 className="text-4xl font-black leading-[1.08] tracking-[-0.035em] text-slate-950 sm:text-6xl lg:text-7xl">
              Encontre colegas
              <br />
              <span className="relative inline-block text-blue-950">
                Forme grupos ideais
                <span className="absolute -bottom-1.5 left-0 h-1.5 w-full bg-lime-400 sm:-bottom-2 sm:h-2" />
              </span>
            </h1>

            <p className="mx-auto mt-7 max-w-2xl text-base leading-7 text-slate-600 sm:text-lg">
              Chega de improvisar na formação de equipes no WhatsApp. O
              CampusHub conecta estudantes do Instituto Mauá por competências
              técnicas, disponibilidade de horários e afinidade temática para
              projetos e TCCs.
            </p>
          </div>

          <div className="mx-auto mt-20 grid max-w-3xl grid-cols-1 border-y border-slate-200 bg-white sm:grid-cols-3">
            <div className="flex items-center justify-center gap-3 px-5 py-5 sm:border-r sm:border-slate-200">
              <Users size={20} className="text-blue-900" />
              <div className="text-left">
                <p className="text-sm font-bold text-slate-900">
                  Perfis Verificados
                </p>
                <p className="text-xs text-slate-500">
                  Apenas alunos da comunidade Mauá
                </p>
              </div>
            </div>

            <div className="flex items-center justify-center gap-3 border-t border-slate-200 px-5 py-5 sm:border-r sm:border-t-0 sm:border-slate-200">
              <Boxes size={20} className="text-lime-600" />
              <div className="text-left">
                <p className="text-sm font-bold text-slate-900">
                  Stack de Habilidades
                </p>
                <p className="text-xs text-slate-500">
                  Soft e hard skills catalogadas
                </p>
              </div>
            </div>

            <div className="flex items-center justify-center gap-3 border-t border-slate-200 px-5 py-5 sm:border-t-0">
              <Network size={20} className="text-amber-600" />
              <div className="text-left">
                <p className="text-sm font-bold text-slate-900">
                  Multidisciplinar
                </p>
                <p className="text-xs text-slate-500">
                  Equipes integrando vários cursos
                </p>
              </div>
            </div>
          </div>
        </div>
      </section>

      {/* 3. CONTEXTO E O PROBLEMA (SEM BOXES) */}
      <section
        id="proposito"
        className="border-b border-slate-200 bg-white py-24"
      >
        <div className="mx-auto max-w-7xl px-6 sm:px-8">
          <div className="grid grid-cols-1 items-start gap-12 lg:grid-cols-12 lg:gap-16">
            {/* Coluna da Esquerda: O Desafio */}
            <div className="lg:col-span-6">
              <span className="text-xs font-bold uppercase tracking-[0.2em] text-blue-900">
                O Desafio Acadêmico
              </span>
              <h2 className="mt-3 text-3xl font-black tracking-tight text-slate-950 sm:text-4xl">
                Trabalhos em grupo não deveriam depender apenas de sorte.
              </h2>
              <p className="mt-6 text-base leading-relaxed text-slate-600">
                Todo semestre o ciclo se repete: prazos apertados para registrar
                grupos, amigos que têm boa convivência mas que dominam
                exatamente as mesmas áreas, ou alunos dedicados que ficam sem
                equipe por falta de rede de contatos no campus.
              </p>
              <p className="mt-4 text-base leading-relaxed text-slate-600">
                O CampusHub foi desenhado para eliminar essa fricção,
                transformando a formação de squads em um processo baseado em
                afinidade de projeto e complementariedade técnica.
              </p>
            </div>

            {/* Coluna da Direita: Comparativo Editorial Limpo (Sem Box) */}
            <div className="space-y-10 lg:col-span-6">
              {/* Antes */}
              <div className="border-t border-slate-200 pt-6">
                <div className="flex items-center gap-2 text-xs font-bold uppercase tracking-wider text-rose-700">
                  <span className="font-mono text-sm">✕</span>O modelo
                  improvisado
                </div>
                <h3 className="mt-2 text-lg font-bold text-slate-950">
                  Grupos montados às pressas
                </h3>
                <p className="mt-2 text-sm leading-relaxed text-slate-600">
                  Mensagens perdidas em grupos gerais de WhatsApp, equipes com
                  competências repetidas e sobrecarga de trabalho concentrada
                  nos mesmos integrantes.
                </p>
              </div>

              {/* Depois */}
              <div className="border-t border-slate-200 pt-6">
                <div className="flex items-center gap-2 text-xs font-bold uppercase tracking-wider text-emerald-700">
                  <CheckCircle2 size={15} />
                  Com o CampusHub
                </div>
                <h3 className="mt-2 text-lg font-bold text-slate-950">
                  Conexão orientada a habilidades
                </h3>
                <p className="mt-2 text-sm leading-relaxed text-slate-600">
                  Filtros por curso, ano e domínio técnico. Você descobre quem
                  precisa da sua habilidade e convida pessoas que complementam
                  suas lacunas de forma equilibrada.
                </p>
              </div>
            </div>
          </div>
        </div>
      </section>

      {/* 4. RECURSOS PRINCIPAIS */}
      <section
        id="recursos"
        className="border-b border-slate-200 bg-white py-24"
      >
        <div className="mx-auto max-w-7xl px-5 sm:px-8">
          <div className="mb-14 max-w-2xl">
            <span className="text-xs font-bold uppercase tracking-[0.18em] text-blue-900">
              Funcionalidades
            </span>
            <h2 className="mt-3 text-3xl font-black tracking-tight text-slate-950 sm:text-4xl">
              Tudo o que você precisa para estruturar projetos de sucesso.
            </h2>
            <p className="mt-4 max-w-xl text-sm leading-6 text-slate-600">
              Uma ferramenta pensada de ponta a ponta para a dinâmica de
              trabalhos práticos, atividades de laboratório e projetos
              integradores.
            </p>
          </div>

          <div className="grid grid-cols-1 gap-px overflow-hidden border border-slate-200 bg-slate-200 md:grid-cols-3">
            <div className="group bg-white p-8 transition-colors duration-300 hover:bg-blue-950">
              <div className="mb-10 flex h-12 w-12 items-center justify-center rounded-lg bg-blue-100 text-blue-950 transition-colors duration-300 group-hover:bg-white/10 group-hover:text-white">
                <Users size={22} />
              </div>
              <span className="text-xs font-bold text-slate-400 group-hover:text-blue-200">
                01 • Matching Inteligente
              </span>
              <h3 className="mt-3 text-xl font-bold text-slate-950 group-hover:text-white">
                Equipes Balanceadas
              </h3>
              <p className="mt-3 text-sm leading-6 text-slate-500 group-hover:text-blue-100">
                O algoritmo aponta sinergias entre quem manja de software,
                hardware, negócios e prototipagem física para evitar grupos
                desiguais.
              </p>
              <div className="mt-7 flex items-center gap-2 text-xs font-bold text-blue-900 group-hover:text-lime-400">
                Descobrir compatibilidade
                <ArrowRight size={14} />
              </div>
            </div>

            <div className="group bg-white p-8 transition-colors duration-300 hover:bg-blue-950">
              <div className="mb-10 flex h-12 w-12 items-center justify-center rounded-lg bg-lime-100 text-lime-800 transition-colors duration-300 group-hover:bg-white/10 group-hover:text-lime-400">
                <Boxes size={22} />
              </div>
              <span className="text-xs font-bold text-slate-400 group-hover:text-blue-200">
                02 • Mural de Projetos
              </span>
              <h3 className="mt-3 text-xl font-bold text-slate-950 group-hover:text-white">
                Vagas e Convites Abertos
              </h3>
              <p className="mt-3 text-sm leading-6 text-slate-500 group-hover:text-blue-100">
                Tem uma ideia de TCC ou desafio extracurricular mas falta alguém
                de Design ou Elétrica? Publique a vaga e receba solicitações
                diretas.
              </p>
              <div className="mt-7 flex items-center gap-2 text-xs font-bold text-blue-900 group-hover:text-lime-400">
                Ver quadro de avisos
                <ArrowRight size={14} />
              </div>
            </div>

            <div className="group bg-white p-8 transition-colors duration-300 hover:bg-blue-950">
              <div className="mb-10 flex h-12 w-12 items-center justify-center rounded-lg bg-amber-100 text-amber-900 transition-colors duration-300 group-hover:bg-white/10 group-hover:text-white">
                <ShieldCheck size={22} />
              </div>
              <span className="text-xs font-bold text-slate-400 group-hover:text-blue-200">
                03 • Confiança Mútua
              </span>
              <h3 className="mt-3 text-xl font-bold text-slate-950 group-hover:text-white">
                Histórico & Portfólio
              </h3>
              <p className="mt-3 text-sm leading-6 text-slate-500 group-hover:text-blue-100">
                Cada usuário pode anexar links do GitHub, Behance e LinkedIn,
                facilitando avaliar o nível técnico e dedicação antes de selar o
                grupo.
              </p>
              <div className="mt-7 flex items-center gap-2 text-xs font-bold text-blue-900 group-hover:text-lime-400">
                Conhecer perfis
                <ArrowRight size={14} />
              </div>
            </div>
          </div>
        </div>
      </section>

      {/* 5. COMO FUNCIONA (PASSO A PASSO) */}
      <section
        id="como-funciona"
        className="border-b border-slate-200 bg-slate-50 py-24"
      >
        <div className="mx-auto max-w-7xl px-6 sm:px-8">
          <div className="mb-16 max-w-2xl">
            <span className="text-xs font-bold uppercase tracking-[0.18em] text-blue-900">
              Jornada do Aluno
            </span>
            <h2 className="mt-3 text-3xl font-black tracking-tight text-slate-950 sm:text-4xl">
              Do cadastro ao grupo formado.
              <br />
              Sem complicação.
            </h2>
            <p className="mt-4 text-sm leading-6 text-slate-600">
              Etapas diretas para você encontrar colegas compatíveis
              rapidamente.
            </p>
          </div>

          <div className="grid grid-cols-1 gap-6 sm:grid-cols-2 lg:grid-cols-4">
            {/* CARD 01 */}
            <div className="flex flex-col justify-between rounded-2xl border border-slate-200/90 bg-white p-8 shadow-sm transition-all duration-200 hover:-translate-y-1 hover:shadow-md">
              <div>
                <div className="flex items-center justify-between border-b border-slate-100 pb-5">
                  <span className="font-mono text-3xl font-black tracking-tight text-blue-950">
                    Passo 1
                  </span>
                </div>

                <h3 className="mt-6 text-lg font-bold text-slate-950">
                  Autenticação
                </h3>

                <p className="mt-3 text-sm leading-relaxed text-slate-600">
                  Acesso com credencial acadêmica, assegurando que todos os
                  membros são colegas matriculados.
                </p>
              </div>
            </div>

            {/* CARD 02 */}
            <div className="flex flex-col justify-between rounded-2xl border border-slate-200/90 bg-white p-8 shadow-sm transition-all duration-200 hover:-translate-y-1 hover:shadow-md">
              <div>
                <div className="flex items-center justify-between border-b border-slate-100 pb-5">
                  <span className="font-mono text-3xl font-black tracking-tight text-lime-600">
                    Passo 2
                  </span>
                </div>

                <h3 className="mt-6 text-lg font-bold text-slate-950">
                  Mapeamento
                </h3>

                <p className="mt-3 text-sm leading-relaxed text-slate-600">
                  Preenchimento rápido de skills principais, disciplinas
                  cursadas e ferramentas que domina.
                </p>
              </div>
            </div>

            {/* CARD 03 */}
            <div className="flex flex-col justify-between rounded-2xl border border-slate-200/90 bg-white p-8 shadow-sm transition-all duration-200 hover:-translate-y-1 hover:shadow-md">
              <div>
                <div className="flex items-center justify-between border-b border-slate-100 pb-5">
                  <span className="font-mono text-3xl font-black tracking-tight text-amber-600">
                    Passo 3
                  </span>
                </div>

                <h3 className="mt-6 text-lg font-bold text-slate-950">
                  Descoberta
                </h3>

                <p className="mt-3 text-sm leading-relaxed text-slate-600">
                  Pesquise squads que buscam seu perfil ou monte um novo grupo
                  convidando integrantes por afinidade.
                </p>
              </div>
            </div>

            {/* CARD 04 */}
            <div className="flex flex-col justify-between rounded-2xl border border-slate-200/90 bg-white p-8 shadow-sm transition-all duration-200 hover:-translate-y-1 hover:shadow-md">
              <div>
                <div className="flex items-center justify-between border-b border-slate-100 pb-5">
                  <span className="font-mono text-3xl font-black tracking-tight text-slate-400">
                    Passo 4
                  </span>
                </div>

                <h3 className="mt-6 text-lg font-bold text-slate-950">
                  Execução
                </h3>

                <p className="mt-3 text-sm leading-relaxed text-slate-600">
                  Grupo montado com canal direto de comunicação e clareza do
                  papel de cada participante.
                </p>
              </div>
            </div>
          </div>
        </div>
      </section>

      {/* 6. CURSOS ATENDIDOS */}
      <section id="cursos" className="border-b border-slate-200 bg-white py-20">
        <div className="mx-auto max-w-7xl px-5 sm:px-8">
          <div className="mx-auto max-w-3xl text-center">
            <GraduationCap size={32} className="mx-auto text-blue-900" />
            <h2 className="mt-3 text-3xl font-black tracking-tight text-slate-950">
              Conexão entre as diversas frentes do campus
            </h2>
            <p className="mt-3 text-sm text-slate-600">
              O ecossistema universitário se torna mais forte quando áreas
              complementares somam forças em laboratórios e bancadas.
            </p>

            <div className="mt-8 flex flex-wrap items-center justify-center gap-2.5">
              {[
                "Engenharia da Computação",
                "Engenharia Mecânica",
                "Engenharia Elétrica",
                "Engenharia de Produção",
                "Engenharia Química",
                "Design",
                "Administração",
                "Ciência da Computação",
                "Sistemas de Informação",
                "Inteligência Artificial",
              ].map((curso) => (
                <span
                  key={curso}
                  className="rounded-full border border-slate-200 bg-slate-50 px-4 py-1.5 text-xs font-semibold text-slate-700 hover:border-slate-300"
                >
                  {curso}
                </span>
              ))}
            </div>
          </div>
        </div>
      </section>

      {/* 7. PERGUNTAS FREQUENTES (FAQ) */}
      <section id="faq" className="border-b border-slate-200 bg-white py-24">
        <div className="mx-auto max-w-5xl px-6 sm:px-8">
          <div className="mb-16">
            <span className="text-xs font-bold uppercase tracking-[0.2em] text-blue-900">
              Dúvidas Comuns
            </span>
            <h2 className="mt-3 text-3xl font-black tracking-tight text-slate-950 sm:text-4xl">
              Perguntas Frequentes
            </h2>
            <p className="mt-3 max-w-xl text-sm leading-relaxed text-slate-500">
              Respostas diretas sobre regras de acesso, tipos de projetos e
              funcionamento da plataforma.
            </p>
          </div>

          <div className="divide-y divide-slate-200 border-t border-b border-slate-200">
            {/* Pergunta 1 */}
            <div className="grid grid-cols-1 gap-4 py-8 md:grid-cols-12 md:gap-8">
              <div className="md:col-span-5">
                <h3 className="text-base font-bold text-slate-950">
                  Quem pode se cadastrar no CampusHub?
                </h3>
              </div>
              <div className="md:col-span-7">
                <p className="text-sm leading-relaxed text-slate-600">
                  A plataforma é restrita à comunidade acadêmica do Instituto
                  Mauá de Tecnologia. O acesso é validado pelo e-mail
                  institucional do estudante.
                </p>
              </div>
            </div>

            {/* Pergunta 2 */}
            <div className="grid grid-cols-1 gap-4 py-8 md:grid-cols-12 md:gap-8">
              <div className="md:col-span-5">
                <h3 className="text-base font-bold text-slate-950">
                  Serve apenas para trabalhos de semestre ou também para TCC?
                </h3>
              </div>
              <div className="md:col-span-7">
                <p className="text-sm leading-relaxed text-slate-600">
                  Serve para ambos! Você pode sinalizar se está buscando
                  integrantes para um Trabalho de Conclusão de Curso de longa
                  duração ou para uma matéria específica do semestre.
                </p>
              </div>
            </div>

            {/* Pergunta 3 */}
            <div className="grid grid-cols-1 gap-4 py-8 md:grid-cols-12 md:gap-8">
              <div className="md:col-span-5">
                <h3 className="text-base font-bold text-slate-950">
                  Como os alunos combinam as reuniões e tarefas?
                </h3>
              </div>
              <div className="md:col-span-7">
                <p className="text-sm leading-relaxed text-slate-600">
                  Após aceitar o convite e fechar a equipe, o CampusHub libera
                  os contatos diretos (como WhatsApp, Teams ou Discord)
                  cadastrados pelos integrantes.
                </p>
              </div>
            </div>

            {/* Pergunta 4 */}
            <div className="grid grid-cols-1 gap-4 py-8 md:grid-cols-12 md:gap-8">
              <div className="md:col-span-5">
                <h3 className="text-base font-bold text-slate-950">
                  Existe algum custo para utilizar a ferramenta?
                </h3>
              </div>
              <div className="md:col-span-7">
                <p className="text-sm leading-relaxed text-slate-600">
                  Não. O CampusHub é um projeto sem fins lucrativos desenvolvido
                  por estudantes e para estudantes no âmbito do Projeto
                  Integrador Interdisciplinar.
                </p>
              </div>
            </div>
          </div>
        </div>
      </section>

      {/* 8. RODAPÉ */}
      <footer
        id="sobre"
        className="border-t border-gray-800 bg-gray-900 py-10 text-xs text-gray-400"
      >
        <div className="mx-auto flex max-w-7xl flex-col items-center justify-between gap-6 px-4 sm:flex-row">
          <div>
            <p className="font-semibold text-white">
              CampusHub • Projeto Integrador Interdisciplinar
            </p>
            <p className="mt-1 text-gray-500">
              Instituto Mauá de Tecnologia — Campus São Caetano do Sul
            </p>
          </div>
          <div className="flex flex-wrap items-center justify-center gap-3">
            <span>Matheus Giongo</span>
            <span>•</span>
            <span>Ian Pezzuol</span>
            <span>•</span>
            <span>Felipe Zanardo</span>
            <span>•</span>
            <span>Pedro Pinto</span>
            <span>•</span>
            <span>Pedro Noel</span>
          </div>
        </div>
      </footer>
    </main>
  );
}
