# Copywriting Portfolio Site with Admin Panel - Detailed Implementation Plan

## Project Overview
Build a portfolio/blog website for your wife (copywriter) with:
- **Public landing page** displaying featured articles/writing samples
- **Admin panel** for managing blog posts (create, edit, delete, publish/draft)
- **Full Spring Boot backend** with authentication and database
- **Production deployment** to Railway or Render

**Timeline:** 9-10 weeks at 5-7 hours/week
**Target Launch:** Live and functional for your wife to manage content

---

## Phase 1: Setup & Foundation (Weeks 1-2)
**Goal:** Get a working Spring Boot project with basic landing page and database structure
**Time Commitment:** 6 hours
**Key Learning:** Spring Boot setup, JPA entities, Thymeleaf templating, basic deployment

### Week 1: Project Setup & Data Model

#### Task 1.1: Create Spring Boot Project (1.5 hours)
- [x] Go to https://start.spring.io
- [x] Configure project settings:
  - **Project:** Maven
  - **Language:** Java
  - **Spring Boot version:** 4.0.3
  - **Packaging:** JAR
  - **Java version:** 21
  - **Group:** eu.wisniewska
  - **Artifact:** www
- [x] Add dependencies:
  - Spring Web (webmvc)
  - Spring Data JPA
  - H2 (development) — PostgreSQL planned for production
  - Thymeleaf
  - Spring Security
  - Lombok
- [x] Configuration file format: **application.yaml** (YAML format)
- [x] Open the project in IntelliJ IDEA
- [x] Verify it runs:
  ```bash
  ./mvnw spring-boot:run
  ```
  Shows "Whitelabel Error Page" (normal—no routes yet)
- [x] Initial Git commit

**Deliverable:** Spring Boot app that starts without errors, committed to Git

#### Task 1.2: Configure Database & Create Post Entity (1.5 hours)
- [x] H2 in-memory database configured in `application.yaml`
- [ ] Create `Post` JPA entity class in `src/main/java/eu/wisniewska/www/entity/Post.java`:
  - Fields: `id` (Long), `title` (String), `content` (String), `excerpt` (String), `slug` (String), `featured` (Boolean), `published` (Boolean), `createdAt` (LocalDateTime), `updatedAt` (LocalDateTime)
  - Use `@Entity`, `@Table`, `@Id`, `@GeneratedValue`, `@Column` annotations
  - Add getters/setters or use Lombok `@Data`
- [ ] Create `PostRepository` interface extending `JpaRepository<Post, Long>` in `src/main/java/eu/wisniewska/www/repository/`
- [ ] Test the setup by running the app—it should create the `post` table in H2
- [ ] Commit: "Add Post entity and PostRepository"

**Deliverable:** Database table created, able to insert/retrieve posts via JPA

#### Task 1.3: Create Basic Controllers & Landing Page (2 hours)
- [x] Create `IndexController` class:
  - Route: `GET /` → render landing page (basic)
  - TODO: Fetch featured posts from database
  - TODO: Pass to Thymeleaf template
- [x] Create `index.html` in `src/main/resources/templates/` (basic template)
  - TODO: Display your wife's bio/headline
  - TODO: Display featured posts (title, excerpt, link to full post)
  - TODO: Add a simple CSS file in `src/main/resources/static/css/style.css` for basic styling
- [ ] Create `post.html` template to display a single full post:
  - Route: `GET /post/{slug}` → fetch post by slug, display full content
- [ ] Add basic HTML structure and styling (keep it simple—focus on function, not design)
- [x] Test locally: Visit http://localhost:8181, see landing page
- [ ] Commit: "Add landing page and post detail view"

**Deliverable:** Landing page works, displays placeholder posts, basic styling in place

### Week 2: Prepare for Deployment & Add Initial Data

#### Task 2.1: Set Up Git & GitHub (0.5 hours)
- [x] Initialize Git repo locally
- [x] Create a `.gitignore` file
- [x] Create a GitHub repository
- [x] Push code to GitHub
- [x] Set up GitHub Actions CI workflow (build + test steps)

**Deliverable:** Code on GitHub, clean commit history

#### Task 2.2: Configure for Production Deployment (1 hour)
- [x] Create Railway account and connect GitHub repo
- [x] Create PostgreSQL service on Railway
- [x] Configure `application-production.yaml` with PostgreSQL env variables (`PGHOST`, `PGPORT`, `PGDATABASE`, `PGUSER`, `PGPASSWORD`)
- [x] Set `SPRING_PROFILES_ACTIVE=production` in Railway
- [x] Set `APP_ADMIN_USERNAME` and `APP_ADMIN_PASSWORD` in Railway
- [x] Deploy and test — site live at public URL
- [x] Fix `ddl-auto: create-drop` → `update` for production (data loss issue)

**Deliverable:** Site live on the internet at a public URL

#### Task 2.3: Add Sample Data & Polish Landing Page (1.5 hours)
- [ ] Create a small SQL script or Java `@PostConstruct` method to insert 3-4 sample posts with your wife's writing
- [ ] Polish `home.html`: better styling, cleaner layout
- [ ] Add a simple navigation bar (Home, Blog, Contact—contact can be a placeholder)
- [ ] Test on production—should see posts displayed
- [ ] Commit: "Add sample data and improve landing page styling"

**Deliverable:** Live production site with posts displaying, ready for next phase

---

## Phase 2: Backend CRUD & Authentication (Weeks 3-4)
**Goal:** Build admin panel endpoints so your wife can create/edit/delete posts
**Time Commitment:** 7 hours
**Key Learning:** REST APIs, Spring Security, request/response handling, form submission

### Week 3: Authentication & Admin Endpoints

#### Task 3.1: Set Up Spring Security (1.5 hours)
- [x] Create `SecurityConfig` class with `SecurityFilterChain` bean:
  - Configure `BCryptPasswordEncoder`
  - Default Spring login page (no custom login page yet)
  - Allow unauthenticated access to `/`, `/actuator/**`
  - Require `ADMIN` role for `/_admin/**`
  - Logout functionality configured
- [x] In-memory service account with credentials from `application.yaml` (`app.admin.*` properties)
- [x] Credentials overridable via environment variables in production
- [x] Create `AdminUser` JPA entity (UUID id, username, password, role, createdAt, updatedAt)
- [x] Create `AdminUserRepository` extending `JpaRepository<AdminUser, UUID>`
- [x] Create `AdminUserService` with `findAll()`, `save()` (with password hashing), `delete()`
- [x] Unit tests for service (Mockito mocks, ArgumentCaptor for password hashing)
- [ ] Create `CustomUserDetailsService` to allow database-backed users to log in
- [x] Test locally: Visit `/_admin`, login with service account, redirect to admin panel

**Deliverable:** Login page works, authentication enforced

#### Task 3.2: Build Admin Dashboard Structure (1.5 hours)
- [x] Create `AdminController`:
  - Route: `GET /_admin` → admin dashboard
- [x] Create `AdminUserController`:
  - Route: `GET /_admin/users` → list all admin users
  - Route: `GET /_admin/users/add` → form to create new admin user
  - Route: `POST /_admin/users/save` → save new admin user (with validation)
  - Route: `POST /_admin/users/delete/{id}` → delete admin user
- [x] Create admin templates with Thymeleaf fragments (`admin/fragments.html` for nav and head)
  - `admin/index.html` → dashboard
  - `admin/users/index.html` → user listing table
  - `admin/users/add.html` → add user form with validation
- [x] Active page highlighting in nav via `@ModelAttribute`
- [x] Controller tests with MockMvc (auth, anonymous, validation with parameterized tests)
- [ ] TODO: Post management routes and templates
- [x] Test locally: Log in, navigate to /_admin, manage users

**Deliverable:** Admin dashboard accessible, user management working

#### Task 3.3: Implement CRUD Endpoints (1.5 hours)
- [ ] Add POST endpoint to `AdminController`:
  - `POST /admin/posts/save` → save new or updated post to database
  - Validate form input (title required, content required)
  - Redirect to posts list on success, show error on failure
- [ ] Add DELETE endpoint:
  - `POST /admin/posts/{id}/delete` → delete post, redirect to posts list
- [ ] Add GET endpoint for editing:
  - `GET /admin/posts/{id}/edit` → fetch post by ID, pass to form template
- [ ] Update `home.html` to only show published posts
- [ ] Test locally: Create a post via admin, see it on homepage; edit and delete posts
- [ ] Test in production: Deploy changes, verify admin panel works
- [ ] Commit: "Add CRUD operations for posts"

**Deliverable:** Can create, read, update, delete posts via admin panel

#### Task 3.4: Add Form Validation & Error Handling (1 hour)
- [ ] Add validation annotations to `Post` entity:
  - `@NotBlank` on title, content
  - `@Size` on title (min 5, max 200 chars)
  - `@NotEmpty` on content
- [ ] Update form submission to display validation errors
- [ ] Add a flash message system to show success/error messages after actions
- [ ] Test: Try submitting empty form, see validation errors
- [ ] Commit: "Add form validation and error handling"

**Deliverable:** Forms validate input, user sees helpful error messages

### Week 4: Polish Admin Panel & Prepare for Real Use

#### Task 4.1: Improve Post Management UI (1.5 hours)
- [ ] Add search functionality:
  - Text input on posts list to filter by title
  - Create endpoint: `GET /admin/posts/search?q={query}`
- [ ] Add sorting:
  - Sort by date created, date updated, featured status
- [ ] Add pagination:
  - Show 10 posts per page
  - Navigation controls
- [ ] Add a "Draft" vs "Published" toggle on list view (quick action without editing full post)
- [ ] Test locally and in production
- [ ] Commit: "Add search, sorting, and pagination to posts list"

**Deliverable:** Admin interface is usable for managing many posts

#### Task 4.2: User Management Basics (1 hour)
- [ ] Create `UserController` (optional for now, but good practice):
  - Route: `GET /admin/users` → list users (admin only)
  - Route: `POST /admin/users/register` → create new user (for adding more admins later)
- [ ] Add basic user registration form (or skip if just your wife using it)
- [ ] Add "change password" functionality for logged-in user
- [ ] Test: Ensure only authenticated users can see user management
- [ ] Commit: "Add basic user management"

**Deliverable:** Can manage user accounts if needed

#### Task 4.3: Add Contact Form (Optional but Quick) (1 hour)
- [ ] Create a simple contact form on landing page:
  - Fields: name, email, message
  - Route: `POST /contact` → send email (use Spring Mail or simple persistence)
- [ ] Option A (simple): Save to database, display in admin panel
- [ ] Option B (better): Configure email sending (Gmail SMTP)
- [ ] Add validation and success message
- [ ] Test locally
- [ ] Commit: "Add contact form"

**Deliverable:** Visitors can contact your wife from site

#### Task 4.4: Final Polish & Testing (1 hour)
- [ ] Review all pages for typos, broken links, styling issues
- [ ] Test on mobile (responsive design)
- [ ] Test all admin features work smoothly
- [ ] Update your wife's bio, real writing samples
- [ ] Deploy to production, verify everything works
- [ ] Commit: "Final polish for phase 2"

**Deliverable:** Admin panel fully functional, site ready for real use

---

## Phase 3: Enhancement & Optimization (Weeks 5-6)
**Goal:** Add nice-to-have features and improve user experience
**Time Commitment:** 5-6 hours (or skip if time is tight)
**Key Learning:** Performance, user experience, advanced features

### Week 5: Content Management Improvements

#### Task 5.1: Rich Text Editor for Posts (1.5 hours)
- [ ] Add a library like `TinyMCE` or `Quill` to the post form
- [ ] Allow your wife to format text (bold, italic, lists, links) when writing
- [ ] Store formatted HTML in database
- [ ] Display formatted content on public post view
- [ ] Test: Write a post with formatting, verify it displays correctly
- [ ] Commit: "Add rich text editor for post content"

**Deliverable:** Posts can have formatted content

#### Task 5.2: Add Categories/Tags (1 hour)
- [ ] Create a `Category` or `Tag` entity
- [ ] Add relationship from `Post` to categories (many-to-many)
- [ ] Update post form to select categories
- [ ] Add filtering by category on landing page and admin
- [ ] Test locally and in production
- [ ] Commit: "Add categories/tags to posts"

**Deliverable:** Posts can be organized by category, filterable

#### Task 5.3: Add Image/Media Support (1.5 hours)
- [ ] Allow uploading a featured image for posts
- [ ] Store images (option: upload to local filesystem or AWS S3, start with local for simplicity)
- [ ] Display featured image on landing page and post detail
- [ ] Add image preview in admin
- [ ] Test: Upload an image, verify it displays
- [ ] Commit: "Add image upload for posts"

**Deliverable:** Posts can have featured images

### Week 6: Performance & User Experience

#### Task 6.1: Optimize Database Queries (1 hour)
- [ ] Review logging to see N+1 query problems
- [ ] Add `@Transactional` annotations where needed
- [ ] Use `@Fetch(FetchType.EAGER)` or explicit joins for relationships
- [ ] Test: Verify fewer database queries are running
- [ ] Commit: "Optimize database queries"

**Deliverable:** Site loads faster

#### Task 6.2: Add Caching (1 hour)
- [ ] Add Spring Cache abstraction:
  - `@EnableCaching` on main class
  - `@Cacheable` on frequently-accessed methods (e.g., featured posts)
- [ ] Test: Verify data is cached
- [ ] Add cache invalidation when posts are updated
- [ ] Commit: "Add caching for performance"

**Deliverable:** Featured posts load from cache

#### Task 6.3: SEO & Analytics (1 hour)
- [ ] Add meta tags to pages (title, description, keywords)
- [ ] Generate dynamic meta tags based on post content
- [ ] Add Open Graph tags for social sharing
- [ ] Add Google Analytics (or similar) script
- [ ] Test: Share a post link, verify preview looks good
- [ ] Commit: "Add SEO and analytics"

**Deliverable:** Posts share nicely on social media, analytics enabled

#### Task 6.4: Mobile Responsiveness & UI Polish (1 hour)
- [ ] Test all pages on mobile
- [ ] Fix any layout issues
- [ ] Improve navigation on mobile (hamburger menu if needed)
- [ ] Improve form usability on mobile
- [ ] Final design pass—make it look professional
- [ ] Commit: "Final mobile and UI polish"

**Deliverable:** Site looks great on all devices

---

## Phase 4: Production Refinement (Weeks 7-9)
**Goal:** Handle real-world usage, fix bugs, ensure reliability
**Time Commitment:** 6-8 hours
**Key Learning:** Debugging, production concerns, monitoring

### Week 7: Real-World Testing & Bug Fixes

#### Task 7.1: Testing with Your Wife (2 hours)
- [ ] Have your wife use the site and admin panel for a week
- [ ] Document all bugs, confusing UX, feature requests
- [ ] Create GitHub issues for each bug
- [ ] Prioritize: critical (site broken) > important (feature doesn't work) > nice-to-have (UX improvement)

**Deliverable:** List of real bugs to fix

#### Task 7.2: Fix Critical Bugs (1.5 hours)
- [ ] Work through critical bugs one by one
- [ ] Write a test for each bug, verify test fails, fix code, verify test passes
- [ ] Deploy fixes to production
- [ ] Test in production with your wife
- [ ] Commit: "Fix critical bugs from real usage"

**Deliverable:** Site is stable and reliable

#### Task 7.3: Handle Edge Cases (1 hour)
- [ ] Very long post titles (truncate in admin list?)
- [ ] Very long content (pagination?)
- [ ] Deleting posts that are linked elsewhere
- [ ] Concurrent edits (what if two admins edit same post?)
- [ ] Test edge cases, add guards
- [ ] Commit: "Handle edge cases"

**Deliverable:** Site handles unusual situations gracefully

### Week 8: Logging, Monitoring, & Security

#### Task 8.1: Add Logging (1 hour)
- [ ] Configure SLF4J/Logback in your app
- [ ] Add meaningful log statements at key points (login, post creation, errors)
- [ ] Configure log levels (DEBUG in dev, INFO in production)
- [ ] Test: Check logs for useful debugging info
- [ ] Commit: "Add comprehensive logging"

**Deliverable:** Can debug issues via logs

#### Task 8.2: Security Hardening (1.5 hours)
- [ ] Add HTTPS (Railway/Render handles this automatically)
- [ ] Set secure headers in `SecurityConfig`:
  - Content-Security-Policy
  - X-Frame-Options
  - X-Content-Type-Options
- [ ] Add CSRF protection (Spring Security does this by default, verify it's on)
- [ ] Escape HTML in templates to prevent XSS attacks (Thymeleaf does this by default)
- [ ] Test: Use OWASP ZAP or similar to scan for vulnerabilities
- [ ] Commit: "Add security hardening"

**Deliverable:** Site is secure against common attacks

#### Task 8.3: Environment Configuration (1 hour)
- [ ] Remove hardcoded values (database URL, passwords, API keys)
- [ ] Use environment variables for all sensitive config
- [ ] Create `.env.example` file showing what variables are needed
- [ ] Update deployment (Railway/Render) to use correct env vars
- [ ] Test: Verify site works with env vars in production
- [ ] Commit: "Use environment variables for configuration"

**Deliverable:** Code doesn't contain secrets

### Week 9: Final Polish & Documentation

#### Task 9.1: Documentation (1 hour)
- [ ] Write `README.md` explaining:
  - What the project does
  - How to set up locally
  - How to deploy
  - Technology stack
  - Your learning outcomes
- [ ] Add comments to complex code sections
- [ ] Commit: "Add comprehensive documentation"

**Deliverable:** Others can understand and run your project

#### Task 9.2: Performance Testing (0.5 hours)
- [ ] Test site performance on slow networks (browser dev tools)
- [ ] Optimize if needed (minimize CSS/JS, optimize images)
- [ ] Test with multiple concurrent users if possible
- [ ] Commit: "Performance improvements"

**Deliverable:** Site is fast and responsive

#### Task 9.3: Final Testing & Launch (1.5 hours)
- [ ] Full QA pass: go through every feature, every page
- [ ] Test on multiple browsers
- [ ] Test on mobile and desktop
- [ ] Have your wife do final review
- [ ] Fix any remaining issues
- [ ] Deploy final version
- [ ] Commit: "Final launch-ready version"

**Deliverable:** Site is live and production-ready

#### Task 9.4: Set Up Monitoring & Alerts (Optional) (0.5 hours)
- [ ] Set up basic uptime monitoring (StatusCake, Updown.io)
- [ ] Configure error alerts (Sentry for Java apps)
- [ ] Test: Trigger an error, verify you get alerted
- [ ] Commit: "Add monitoring and alerting"

**Deliverable:** You'll know if something breaks

---

## Week 10: Buffer & Contingency
**Goal:** Catch anything unexpected, final tweaks
- Fix any remaining bugs
- Respond to production issues
- Make final improvements based on usage
- Celebrate launch! 🎉

---

## Summary of Skills You'll Have Gained

### Core Spring/Java
- Spring Boot project setup and configuration
- Spring Data JPA (database operations)
- Spring Security (authentication, authorization)
- Spring Web (REST controllers, MVC)
- Thymeleaf templating
- Entity relationships and JPA annotations

### Full-Stack Development
- Database design (schema, relationships)
- REST API design
- Form handling and validation
- Authentication and authorization
- Error handling and logging
- Environment configuration

### Production & DevOps
- Deployment to cloud (Railway/Render)
- Environment variables and secrets management
- Database migration and optimization
- Security hardening
- Monitoring and logging

### Real-World Skills
- Git version control and GitHub
- Debugging production issues
- User testing and feedback incorporation
- Documentation writing
- Performance optimization

---

## Tips for Success

1. **Ship imperfect work early**: After week 2, deploy something live. Perfect is the enemy of done.

2. **Test with your wife**: Real usage catches bugs and design issues you'll never find alone.

3. **Commit frequently**: Small, focused commits make debugging easier and show good development practices.

4. **Don't skip phases**: Each phase builds skills for the next. Don't jump to fancy features before CRUD works.

5. **If you fall behind**: Skip Phase 3 (enhancements). Phase 1-2 + 4 is a solid, complete project.

6. **Document as you go**: Future you (and interviewers) will thank you.

7. **Use this plan with Claude Code**: Feed each week's tasks into Claude Code for implementation help and code review.

---

## Project Structure Reference

```
www.wisniewska.eu/
├── src/main/java/eu/wisniewska/www/
│   ├── WwwApplication.java
│   ├── controller/
│   │   ├── HomeController.java
│   │   ├── AdminController.java
│   │   └── UserController.java
│   ├── entity/
│   │   ├── Post.java
│   │   └── User.java
│   ├── repository/
│   │   ├── PostRepository.java
│   │   └── UserRepository.java
│   ├── service/
│   │   ├── PostService.java
│   │   └── UserService.java
│   └── config/
│       └── SecurityConfig.java
├── src/main/resources/
│   ├── templates/
│   │   ├── home.html
│   │   ├── post.html
│   │   ├── contact.html
│   │   └── admin/
│   │       ├── dashboard.html
│   │       ├── posts-list.html
│   │       └── post-form.html
│   ├── static/
│   │   ├── css/style.css
│   │   └── js/script.js
│   └── application.yaml
├── pom.xml
├── README.md
└── .gitignore
```

---

## Next Steps

1. Read this plan through once
2. Start **Week 1, Task 1.1** (create Spring Boot project)
3. After completing each task, feed that week's tasks into Claude Code with: "Help me complete Week X of my portfolio project: [paste relevant tasks]"
4. Commit your work after each task
5. Stay consistent at 5-7 hours/week
6. Enjoy the process—you're building something real!
