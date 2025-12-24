# SixthSenzMAD
WIA2007 Mobile App Development Project

# JobMatch 📱  
**Empowering Equal Access to Employment Opportunities**

---

## 🌍 About JobMatch
**JobMatch** is a mobile application designed to support **UN Sustainable Development Goal 10: Reduced Inequalities**, with a focus on promoting **equal rights and opportunities in employment**.

The app bridges gaps faced by job seekers by providing fair access to job postings, personalized job recommendations, skill-building training content, and a community-driven forum.  
By integrating employment, learning, and discussion in one platform, JobMatch helps users improve employability regardless of background.

## ✨ Features

### 🏠 Home Page
- Central dashboard for app navigation
- Displays relevant job opportunities and recommendations

### 🔐 Login & Authentication
- User login system
- Role-based access (e.g. employer vs job seeker)
- Persistent login using local storage

### 💼 Job Posting
- Employers can create and manage job listings
- Job seekers can browse available positions

### 🎯 Job Recommendations
- Personalized job recommendations
- Matches users with relevant job opportunities

### 🔍 Job Search
- Search jobs by keyword or category
- Simple and intuitive filtering

### 💬 Chat List
- Displays user conversations
- Enables communication between employers and job seekers

### 🎓 Training Content
- Learning materials and short courses
- Helps users develop skills and prepare for employment

### 🗣️ Forum
- Course-based discussion forums
- Users can create posts and comments
- Encourages peer learning and knowledge sharing

### 👤 Profile Page
- Displays user details and role
- Allows basic profile management

---

## ⚙️ Technical Overview (Behind the Scenes)

- **Platform:** Android (Java)
- **Architecture:** MVVM (where applicable)
- **Database:** Room (AppDatabase)

### Stored Data
- Users
- Job postings
- Training courses
- Forum posts
- Comments

### Key Technologies
- **Room + LiveData**
  - Automatically updates UI when database data changes
  - Used for courses, forum posts, and comments
- **RecyclerView**
  - Displays dynamic lists (jobs, courses, forum posts, comments)
- **SharedPreferences**
  - Stores login state and logged-in username
- **Role-Based Access Control**
  - Different features for employers and job seekers

---

## 🎯 SDG Alignment
This project supports **SDG 10 – Reduced Inequalities** by:
- Improving access to employment opportunities
- Providing inclusive skill development resources
- Encouraging community participation through forums

---

## 🚀 Future Enhancements
- Cloud-based authentication
- Advanced job recommendation algorithms
- Real-time chat
- Analytics for job trends and skill demand
