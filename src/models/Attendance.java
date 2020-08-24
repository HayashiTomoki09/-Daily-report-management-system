package models;

import java.sql.Date;
import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;


@Table(name="attendance")
@NamedQueries({
    @NamedQuery(//ログインIDのログイン日に出退社入力をしているか確認
                name="today'sAttendancesCheck",
                query="SELECT Count(a) FROM Attendance AS a WHERE a.employee_id = :employee AND a.attendance_date = :report_date"),
    @NamedQuery(//出退社情報の取得
            name="Attendance",
            query="SELECT a FROM Attendance AS a WHERE a.employee_id = :employee AND a.attendance_date = :report_date"),
    })
@Entity
public class Attendance {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "employee_id" ,nullable = false)
    private Employee employee_id;

    @Column(name = "cometime",nullable = false)
    private Timestamp cometime;

    @Column(name = "leavetime")
    private Timestamp leavetime;

    @Column(name = "attendance_date" ,nullable = false)
    private Date attendance_date;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Employee getEmployee_id() {
        return employee_id;
    }

    public void setEmployee_id(Employee employee_id) {
        this.employee_id = employee_id;
    }

    public Timestamp getCometime() {
        return cometime;
    }

    public void setCometime(Timestamp cometime) {
        this.cometime = cometime;
    }

    public Timestamp getLeavetime() {
        return leavetime;
    }

    public void setLeavetime(Timestamp leavetime) {
        this.leavetime = leavetime;
    }

    public Date getAttendance_date() {
        return attendance_date;
    }

    public void setAttendance_date(Date attendance_date) {
        this.attendance_date = attendance_date;
    }
}
