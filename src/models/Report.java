package models;


import java.sql.Date;
import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

@Table(name = "reports")
@NamedQueries({
    @NamedQuery(//全レポートを取得
            name = "getAllReports",
            query = "SELECT r FROM Report AS r ORDER BY r.id DESC"
            ),
    @NamedQuery(//全レポート数を取得
            name = "getReportsCount",
            query = "SELECT COUNT(r) FROM Report AS r"
            ),
    @NamedQuery(//ログイン社員の全レポートを取得
            name = "getMyAllReports",
            query = "SELECT r FROM Report AS r WHERE r.employee = :employee ORDER BY r.id DESC"
            ),
    @NamedQuery(//ログイン社員の全レポート数を取得
            name = "getMyReportsCount",
            query = "SELECT COUNT(r) FROM Report AS r WHERE r.employee = :employee"
            ),
    @NamedQuery(//社員名検索のレポート取得
            name = "getFindReportsName",
            query = "SELECT r FROM Report AS r WHERE r.employee.name LIKE :name ORDER BY r.id DESC"
            ),
    @NamedQuery(//社員名検索のレポート数取得
            name = "getFindReportsNameCount",
            query = "SELECT COUNT(r) FROM Report AS r WHERE r.employee.name LIKE :name"
            ),
    @NamedQuery(//タイトル検索のレポート取得
            name = "getFindReportsTitle",
            query = "SELECT r FROM Report AS r WHERE r.title LIKE :name ORDER BY r.title DESC"
            ),
    @NamedQuery(//タイトル検索のレポート数取得
            name = "getFindReportsTitleCount",
            query = "SELECT COUNT(r) FROM Report AS r WHERE r.title LIKE :name"
            ),
    @NamedQuery(//ログイン社員のフォロー社員のみのレポート取得
            name = "getFindReportsFollow",
            query = "SELECT r FROM Report AS r , Follow AS f WHERE r.employee = f.followed_id AND f.follower_id = :logid"
            ),
    @NamedQuery(//ログイン社員のフォロー社員のみのレポート数取得
            name = "getFindReportsFollowCount",
            query = "SELECT COUNT(r) FROM Report AS r , Follow AS f WHERE r.employee = f.followed_id AND f.follower_id = :logid"
            ),
    @NamedQuery(//日付け検索のレポート取得
            name = "getFindReportsDate",
            query = "SELECT r FROM Report AS r WHERE r.report_date LIKE :date ORDER BY r.id DESC"
            ),
    @NamedQuery(//日付け検索のレポート数取得
            name = "getFindReportsDateCount",
            query = "SELECT COUNT(r) FROM Report AS r WHERE r.report_date LIKE :date"
            ),
})
@Entity
public class Report {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "employee_id", nullable = false)
    private Employee employee;

    @Column(name = "report_date", nullable = false)
    private Date report_date;

    @Column(name = "title", length = 255, nullable = false)
    private String title;

    @Lob
    @Column(name = "content", nullable = false)
    private String content;

    @Column(name = "created_at", nullable = false)
    private Timestamp created_at;

    @Column(name = "updated_at", nullable = false)
    private Timestamp updated_at;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Employee getEmployee() {
        return employee;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
    }

    public Date getReport_date() {
        return report_date;
    }

    public void setReport_date(Date report_date) {
        this.report_date = report_date;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Timestamp getCreated_at() {
        return created_at;
    }

    public void setCreated_at(Timestamp created_at) {
        this.created_at = created_at;
    }

    public Timestamp getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(Timestamp updated_at) {
        this.updated_at = updated_at;
    }

}
