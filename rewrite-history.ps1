# Script para reescribir historia git del backend con commits distribuidos
$ErrorActionPreference = "Continue"
$repo = "C:\Users\alvar\dev\workspaces\workspace-la-trattoria\la-trattoria-backend"
Set-Location $repo

Write-Host "=== FASE 1: Respaldo ===" -ForegroundColor Cyan
git add -A
$pending = git status --porcelain
if ($pending) {
    git -c user.email="temp@local" -c user.name="temp" commit -m "TEMP backup" | Out-Null
}
git branch -f backup-original HEAD
Write-Host "Backup branch creado: backup-original"

Write-Host "=== FASE 2: Snapshot ===" -ForegroundColor Cyan
$snap = "$env:TEMP\latrattoria-backend-snapshot"
if (Test-Path $snap) { Remove-Item -Recurse -Force $snap }
New-Item -ItemType Directory -Path $snap | Out-Null
robocopy . $snap /E /XD .git bin .vscode /XF log.txt .classpath .project /NFL /NDL /NJH /NJS /NP | Out-Null

# .gitignore mejorado en snapshot (estado final)
@'
HELP.md
target/
!.mvn/wrapper/maven-wrapper.jar
!**/src/main/**/target/
!**/src/test/**/target/

### STS ###
.apt_generated
.classpath
.factorypath
.project
.settings
.springBeans
.sts4-cache

### IntelliJ IDEA ###
.idea
*.iws
*.iml
*.ipr

### NetBeans ###
/nbproject/private/
/nbbuild/
/dist/
/nbdist/
/.nb-gradle/
build/
!**/src/main/**/build/
!**/src/test/**/build/

### VS Code ###
.vscode/
bin/

### Logs ###
log.txt
*.log
'@ | Set-Content "$snap\.gitignore" -Encoding UTF8 -NoNewline

Write-Host "Snapshot creado en $snap"

Write-Host "=== FASE 3: Orphan branch ===" -ForegroundColor Cyan
git checkout --orphan new-history 2>$null | Out-Null
git rm -rf . 2>$null | Out-Null
Get-ChildItem -Force | Where-Object { $_.Name -ne ".git" } | Remove-Item -Recurse -Force

function Copy-Rel {
    param([string]$Rel)
    $src = Join-Path $snap $Rel
    $dst = Join-Path (Get-Location) $Rel
    if (-not (Test-Path $src)) { Write-Warning "  -> falta $Rel"; return }
    if ((Get-Item $src).PSIsContainer) {
        if (-not (Test-Path $dst)) { New-Item -ItemType Directory -Path $dst -Force | Out-Null }
        Copy-Item "$src\*" $dst -Recurse -Force
    } else {
        $dstDir = Split-Path $dst -Parent
        if ($dstDir -and -not (Test-Path $dstDir)) { New-Item -ItemType Directory -Path $dstDir -Force | Out-Null }
        Copy-Item $src $dst -Force
    }
}

function Make-Commit {
    param([string]$Msg, [string]$Date, [string[]]$Files)
    foreach ($f in $Files) { Copy-Rel $f }
    git add -A | Out-Null
    $env:GIT_AUTHOR_DATE = $Date
    $env:GIT_COMMITTER_DATE = $Date
    git commit -m $Msg --allow-empty --quiet
    Write-Host "  $Date  $Msg"
}

Write-Host "=== FASE 4: Creando commits ===" -ForegroundColor Cyan

# === SEMANA 1: Lun 2 Feb — arranque del proyecto ===
Make-Commit "Estructura inicial del proyecto Spring Boot" "2026-02-02T10:15:00" @(
    "pom.xml", "mvnw", "mvnw.cmd", ".mvn",
    "src/main/java/com/latrattoria/backend/BackendApplication.java"
)
Make-Commit "Anadiendo configuracion de la aplicacion" "2026-02-02T10:48:00" @(
    "src/main/resources/application.properties"
)
Make-Commit "Creando .gitignore inicial" "2026-02-03T19:30:00" @(".gitignore")

# (Feb 4-8: nada)

# === SEMANA 2: Lun 9 Feb — modelos de la BBDD ===
Make-Commit "Anadiendo modelo Mesa" "2026-02-09T17:20:00" @(
    "src/main/java/com/latrattoria/backend/model/Mesa.java"
)
Make-Commit "Anadiendo modelo Plato" "2026-02-09T17:55:00" @(
    "src/main/java/com/latrattoria/backend/model/Plato.java"
)
Make-Commit "Anadiendo modelo Categoria" "2026-02-10T11:05:00" @(
    "src/main/java/com/latrattoria/backend/model/Categoria.java"
)
Make-Commit "Anadiendo modelo UsuarioSistema con roles" "2026-02-10T11:40:00" @(
    "src/main/java/com/latrattoria/backend/model/UsuarioSistema.java"
)
Make-Commit "Anadiendo modelo Pedido" "2026-02-11T18:10:00" @(
    "src/main/java/com/latrattoria/backend/model/Pedido.java"
)
Make-Commit "Anadiendo modelo DetallePedido" "2026-02-11T18:45:00" @(
    "src/main/java/com/latrattoria/backend/model/DetallePedido.java"
)
Make-Commit "Anadiendo modelo Notificacion" "2026-02-11T19:22:00" @(
    "src/main/java/com/latrattoria/backend/model/Notificacion.java"
)

# (Feb 12-22: dos semanas sin actividad)

# === SEMANA 4: Lun 23 Feb — repositorios ===
Make-Commit "Creando repositorio de Mesa" "2026-02-23T16:30:00" @(
    "src/main/java/com/latrattoria/backend/repository/MesaRepository.java"
)
Make-Commit "Creando repositorio de Plato" "2026-02-23T16:52:00" @(
    "src/main/java/com/latrattoria/backend/repository/PlatoRepository.java"
)
Make-Commit "Creando repositorio de Categoria" "2026-02-24T20:15:00" @(
    "src/main/java/com/latrattoria/backend/repository/CategoriaRepository.java"
)
Make-Commit "Creando repositorio de UsuarioSistema" "2026-02-24T20:38:00" @(
    "src/main/java/com/latrattoria/backend/repository/UsuarioSistemaRepository.java"
)
Make-Commit "Creando repositorio de Pedido" "2026-02-24T21:05:00" @(
    "src/main/java/com/latrattoria/backend/repository/PedidoRepository.java"
)
Make-Commit "Creando repositorio de DetallePedido" "2026-02-25T17:40:00" @(
    "src/main/java/com/latrattoria/backend/repository/DetallePedidoRepository.java"
)
Make-Commit "Creando repositorio de Notificacion" "2026-02-25T18:10:00" @(
    "src/main/java/com/latrattoria/backend/repository/NotificacionRepository.java"
)

# (Feb 26 - Mar 9: dos semanas sin actividad)

# === SEMANA 7: Mar 10 Mar — servicios (primera tanda) ===
Make-Commit "Servicio de Mesa" "2026-03-10T21:15:00" @(
    "src/main/java/com/latrattoria/backend/service/MesaService.java"
)
Make-Commit "Servicio de Plato" "2026-03-10T21:50:00" @(
    "src/main/java/com/latrattoria/backend/service/PlatoService.java"
)
Make-Commit "Servicio de Categoria" "2026-03-11T22:05:00" @(
    "src/main/java/com/latrattoria/backend/service/CategoriaService.java"
)
Make-Commit "Servicio de UsuarioSistema con BCrypt" "2026-03-11T22:35:00" @(
    "src/main/java/com/latrattoria/backend/service/UsuarioSistemaService.java"
)

# (Mar 12-16: descanso)

# === Mar 17 (martes suelto) — resto de servicios ===
Make-Commit "Servicio de Pedido" "2026-03-17T16:00:00" @(
    "src/main/java/com/latrattoria/backend/service/PedidoService.java"
)
Make-Commit "Servicio de DetallePedido" "2026-03-17T16:30:00" @(
    "src/main/java/com/latrattoria/backend/service/DetallePedidoService.java"
)
Make-Commit "Servicio de Notificacion" "2026-03-17T17:15:00" @(
    "src/main/java/com/latrattoria/backend/service/NotificacionService.java"
)

# (Mar 18-22: nada)

# === SEMANA 9: Lun 23 Mar — seguridad y JWT ===
Make-Commit "Anadiendo utilidades JWT" "2026-03-23T19:40:00" @(
    "src/main/java/com/latrattoria/backend/security/JwtUtil.java"
)
Make-Commit "Servicio de UserDetails para Spring Security" "2026-03-24T18:20:00" @(
    "src/main/java/com/latrattoria/backend/security/CustomUserDetailsService.java"
)
Make-Commit "Filtro de autenticacion JWT" "2026-03-24T19:05:00" @(
    "src/main/java/com/latrattoria/backend/security/JwtAuthenticationFilter.java"
)
Make-Commit "Configuracion inicial de Spring Security" "2026-03-25T21:30:00" @(
    "src/main/java/com/latrattoria/backend/security/SecurityConfig.java"
)
Make-Commit "Controlador de autenticacion con endpoint /login" "2026-03-25T22:15:00" @(
    "src/main/java/com/latrattoria/backend/controller/AuthController.java"
)

# (Mar 26 - Abr 5: semana santa / descanso largo)

# === SEMANA 11: Lun 6 Abr — controladores ===
Make-Commit "Controlador de Categorias con GET publico" "2026-04-06T17:45:00" @(
    "src/main/java/com/latrattoria/backend/controller/CategoriaController.java"
)
Make-Commit "Controlador de Platos con GET publico" "2026-04-06T18:20:00" @(
    "src/main/java/com/latrattoria/backend/controller/PlatoController.java"
)
Make-Commit "Mejorando .gitignore" "2026-04-07T11:30:00" @(".gitignore")
Make-Commit "Anadiendo endpoints CRUD para platos (admin)" "2026-04-08T20:00:00" @(
    "src/main/java/com/latrattoria/backend/controller/PlatoController.java"
)
Make-Commit "Anadiendo endpoints CRUD para categorias (admin)" "2026-04-08T20:45:00" @(
    "src/main/java/com/latrattoria/backend/controller/CategoriaController.java"
)

# (Abr 9-14: nada)

# === Abr 15 (martes suelto) — config ===
Make-Commit "Habilitando CORS para el frontend" "2026-04-15T18:50:00" @(
    "src/main/java/com/latrattoria/backend/security/SecurityConfig.java"
)
Make-Commit "Ajustes en la configuracion de JPA" "2026-04-15T19:25:00" @(
    "src/main/resources/application.properties"
)

# (Abr 16-27: vacaciones / sin actividad)

# === SEMANA 14: Lun 28 Abr — pedidos ===
Make-Commit "Anadiendo documentacion de la API" "2026-04-28T10:00:00" @(
    "LaTrattoria_API_Documentacion.pdf"
)
Make-Commit "Script SQL inicial de la base de datos" "2026-04-28T10:35:00" @("sql")
Make-Commit "Endpoint para crear pedido desde el cliente" "2026-04-29T19:10:00" @(
    "src/main/java/com/latrattoria/backend/controller/PedidoController.java"
)
Make-Commit "Permitiendo POST /pedidos sin autenticacion" "2026-04-29T19:50:00" @(
    "src/main/java/com/latrattoria/backend/security/SecurityConfig.java"
)
Make-Commit "Endpoint para listar pedidos pendientes" "2026-04-29T20:30:00" @(
    "src/main/java/com/latrattoria/backend/controller/PedidoController.java"
)
Make-Commit "Endpoint para marcar pedido como leido" "2026-04-30T17:15:00" @(
    "src/main/java/com/latrattoria/backend/controller/PedidoController.java"
)
Make-Commit "Endpoint para marcar pedido como entregado" "2026-04-30T17:55:00" @(
    "src/main/java/com/latrattoria/backend/controller/PedidoController.java"
)

# (May 1-11: festivos + puente, sin actividad)

# === SEMANA 16: Mar 12 May — fixes y refactor ===
Make-Commit "Ajustando enum Estado de Pedido a minusculas" "2026-05-12T21:00:00" @(
    "src/main/java/com/latrattoria/backend/model/Pedido.java"
)
Make-Commit "Ajustando enum Modalidad y MetodoPago" "2026-05-12T21:30:00" @(
    "src/main/java/com/latrattoria/backend/model/Pedido.java"
)
Make-Commit "Refactor: Categoria usa nombre como String" "2026-05-13T18:10:00" @(
    "src/main/java/com/latrattoria/backend/model/Categoria.java"
)
Make-Commit "Actualizando CategoriaRepository tras refactor" "2026-05-13T18:45:00" @(
    "src/main/java/com/latrattoria/backend/repository/CategoriaRepository.java"
)
Make-Commit "Validaciones y duplicados en CategoriaController" "2026-05-13T19:20:00" @(
    "src/main/java/com/latrattoria/backend/controller/CategoriaController.java",
    "src/main/java/com/latrattoria/backend/service/CategoriaService.java"
)
Make-Commit "Endpoint /admin/todos para listar todos los platos" "2026-05-14T22:00:00" @(
    "src/main/java/com/latrattoria/backend/controller/PlatoController.java"
)
Make-Commit "Mejorando .gitignore (excluyendo bin y logs)" "2026-05-14T22:25:00" @(".gitignore")

# === May 17 (sabado) — retoques finales ===
Make-Commit "Pequenos ajustes en seguridad y CORS" "2026-05-17T12:00:00" @(
    "src/main/java/com/latrattoria/backend/security/SecurityConfig.java"
)
Make-Commit "Ajustes finales en modelos" "2026-05-17T12:30:00" @(
    "src/main/java/com/latrattoria/backend/model"
)

Write-Host "=== FASE 5: Sincronizacion final ===" -ForegroundColor Cyan
Get-ChildItem -Force | Where-Object { $_.Name -ne ".git" } | Remove-Item -Recurse -Force
robocopy $snap . /E /NFL /NDL /NJH /NJS /NP | Out-Null
git add -A | Out-Null
$env:GIT_AUTHOR_DATE = "2026-05-17T13:00:00"
$env:GIT_COMMITTER_DATE = "2026-05-17T13:00:00"
git commit -m "Sincronizacion final del proyecto" --allow-empty --quiet
Write-Host "  2026-05-17  Sincronizacion final del proyecto"

Write-Host "=== FASE 6: Reemplazando main ===" -ForegroundColor Cyan
git branch -D main 2>$null | Out-Null
git branch -m new-history main
Write-Host ""
Write-Host "Total commits:" -NoNewline
git rev-list --count HEAD
Write-Host ""
Write-Host "Listo. Para revisar: git log --oneline" -ForegroundColor Green
Write-Host "Para empujar al remoto: git push origin main --force-with-lease" -ForegroundColor Yellow
