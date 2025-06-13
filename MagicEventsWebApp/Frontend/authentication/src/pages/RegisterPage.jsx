import React, { useState } from 'react';

function RegisterPage() {
  const [formData, setFormData] = useState({
    name: '',
    surname: '',
    username: '',
    email: '',
    password: ''
  });

  const handleChange = (e) => {
    setFormData(prev => ({
      ...prev,
      [e.target.name]: e.target.value
    }));
  };

  const handleSubmit = async (e) => {
    e.preventDefault();

    const params = new URLSearchParams();
    for (const key in formData) {
        params.append(key, formData[key]);
    }

    try {
        const res = await fetch("https://localhost:8443/login/register", {
            method: "POST",
            headers: {
                'Content-Type': 'application/x-www-form-urlencoded'
            },
            body: params.toString()
        });

        if (!res.ok) throw new Error("Registration invalid");
        const data = await res;
        console.log("Success:", data);
    } catch (err) {
        console.error("Error:", err.message);
    }
    };

  return (
    <div style={styles.container}>
      <div style={styles.card}>
        <h2 style={styles.title}>Create your account</h2>
        <form onSubmit={handleSubmit} style={styles.form}>
          <div style={styles.fieldGroup}>
            <label style={styles.label}>Name</label>
            <input
              type="text"
              name="name"
              value={formData.name}
              onChange={handleChange}
              style={styles.input}
              required
            />
          </div>

          <div style={styles.fieldGroup}>
            <label style={styles.label}>Surname</label>
            <input
              type="text"
              name="surname"
              value={formData.surname}
              onChange={handleChange}
              style={styles.input}
              required
            />
          </div>

          <div style={styles.fieldGroup}>
            <label style={styles.label}>Username</label>
            <input
              type="text"
              name="username"
              value={formData.username}
              onChange={handleChange}
              style={styles.input}
              required
            />
          </div>

          <div style={styles.fieldGroup}>
            <label style={styles.label}>Email</label>
            <input
              type="email"
              name="email"
              value={formData.email}
              onChange={handleChange}
              style={styles.input}
              required
            />
          </div>

          <div style={styles.fieldGroup}>
            <label style={styles.label}>Password</label>
            <input
              type="password"
              name="password"
              value={formData.password}
              onChange={handleChange}
              style={styles.input}
              required
            />
          </div>

          <button type="submit" style={styles.button}>
            Register
          </button>
        </form>
      </div>
    </div>
  );
}

const styles = {
  container: {
    minHeight: '100vh',
    background: 'linear-gradient(to right, #6a11cb, #2575fc)',
    display: 'flex',
    justifyContent: 'center',
    alignItems: 'center',
    padding: '30px'
  },
  card: {
    background: 'white',
    borderRadius: '10px',
    padding: '40px 30px',
    boxShadow: '0 8px 20px rgba(0,0,0,0.1)',
    width: '100%',
    maxWidth: '400px'
  },
  title: {
    textAlign: 'center',
    marginBottom: '30px',
    fontWeight: 'bold',
    color: '#333'
  },
  form: {
    display: 'flex',
    flexDirection: 'column'
  },
  fieldGroup: {
    marginBottom: '15px'
  },
  label: {
    marginBottom: '5px',
    fontWeight: 'bold',
    fontSize: '14px',
    color: '#444'
  },
  input: {
    width: '100%',
    padding: '10px',
    borderRadius: '6px',
    border: '1px solid #ccc',
    fontSize: '14px'
  },
  button: {
    padding: '12px',
    backgroundColor: '#28a745',
    color: 'white',
    border: 'none',
    fontWeight: 'bold',
    borderRadius: '6px',
    cursor: 'pointer',
    marginTop: '10px',
    transition: 'background-color 0.3s'
  }
};

export default RegisterPage;
