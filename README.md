# Rapport de Projet : Architecture Micro-services Sécurisée
**Technologies : Spring Boot, React, Keycloak, PostgreSQL, Docker, DevSecOps**

## 1. Contexte du Projet
Ce projet a pour objectif la conception et le développement d'une application web moderne basée sur une architecture micro-services hautement sécurisée. L'application permet la gestion d'un catalogue de produits et le passage de commandes, en respectant les standards industriels de sécurité (OAuth2, OIDC) et les principes DevSecOps.

## 2. Architecture Globale
L'architecture est composée de plusieurs composants orchestrés via Docker :

*   **Frontend (React)** : Interface utilisateur SPA sécurisée.
*   **API Gateway (Spring Cloud Gateway)** : Point d'entrée unique, gère le routage et la validation des tokens.
*   **Product Service** : Gestion du catalogue (CRUD) avec base PostgreSQL dédiée.
*   **Order Service** : Gestion des commandes, communication avec le Product Service via OpenFeign.
*   **Keycloak** : Serveur d'identité et d'autorisation.
*   **Databases** : Deux instances PostgreSQL indépendantes pour garantir l'isolation des données.

### Diagramme de flux (Haut niveau)
`Frontend -> Gateway -> Micro-services (Validation JWT via Keycloak)`

## 3. Modèle de Sécurité
La sécurité constitue l'axe central du projet :

*   **Authentification** : Gérée par Keycloak via le protocole OAuth2 / OpenID Connect.
*   **Autorisation (RBAC)** : 
    *   **ADMIN** : Accès complet (Gestion des produits, visualisation de toutes les commandes).
    *   **CLIENT** : Accès restreint (Consultation produits, passage de ses propres commandes).
*   **Propagation de Token** : Le token JWT reçu par la Gateway est propagé aux micro-services. Les appels inter-services (Order -> Product) utilisent un `RequestInterceptor` Feign pour inclure le token de l'utilisateur.
*   **Zero Trust** : Chaque micro-service valide l'authenticité du JWT et vérifie les rôles via des annotations `@PreAuthorize`.

## 4. Implémentation des Micro-services

### Product Service
*   **Entité** : ID, Nom, Prix, Quantité.
*   **Sécurité** : Routes `POST`, `PUT`, `DELETE` protégées pour le rôle `ADMIN`.

### Order Service
*   **Logique métier** : 
    1. Récupération des détails du produit via `ProductRestClient`.
    2. Vérification de la disponibilité du stock.
    3. Calcul du montant total côté serveur.
    4. Enregistrement de la commande et de ses lignes associées.

## 5. Démarche DevSecOps
Le projet intègre une chaîne CI/CD automatisée via GitHub Actions :

*   **SCA (Software Composition Analysis)** : Utilisation de **OWASP Dependency Check** pour identifier les bibliothèques vulnérables.
*   **SAST (Static Application Security Testing)** : Analyse de la qualité du code et détection de vulnérabilités via **SonarQube**.
*   **Container Scanning** : Utilisation de **Trivy** pour scanner les images Docker et détecter des failles dans l'OS ou les dépendances système.
*   **Traçabilité** : Formatage des logs standardisé incluant `user_id` pour chaque requête.

## 6. Guide d'utilisation

### Pré-requis
*   Docker & Docker Compose
*   Java 21 & Maven

### Lancement de l'infrastructure
```bash
docker compose up -d
```

### Accès aux interfaces
*   **Frontend** : `http://localhost:3000` [PLACEHOLDER : INSERT UI SCREENSHOT]
*   **Gateway** : `http://localhost:8085`
*   **Keycloak** : `http://localhost:8080` (Admin: `admin`/`admin`)
*   **pgAdmin** : `http://localhost:5050` (Admin: `admin@admin.com`/`admin`)

## 7. Interfaces (Captures d'écran Placeholders)
*   **Page de Connexion Keycloak** : `[IMAGE_KEYCLOAK_LOGIN]`
*   **Catalogue Produits (Vue Client)** : `[IMAGE_CATALOG_CLIENT]`
*   **Gestion Produits (Vue Admin)** : `[IMAGE_CATALOG_ADMIN]`
*   **Historique des Commandes** : `[IMAGE_ORDERS_LIST]`
*   **Rapport de Sécurité Trivy** : `[IMAGE_TRIVY_REPORT]`

---
*Réalisé dans le cadre du module Micro-services et Sécurité.*
