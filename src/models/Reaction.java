package models;

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


@Table(name="reaction")

@NamedQueries({
    @NamedQuery(//レポートに対する全リアクション取得（ログイン社員以外）
            name = "getReactionsAll",
            query = "SELECT r FROM Reaction AS r WHERE r.report_id = :report_id AND r.employee_id <> :logid"),
    @NamedQuery(//レポートに対するリアクション種類別カウント取得
            name = "getReactionsTypeCount",
            query = "SELECT count(r) FROM Reaction AS r WHERE r.report_id = :report_id AND r.type = :typeNum"),
    @NamedQuery(//レポートに対するログイン社員のリアクション数を取得
            name = "getLoginReactionCount",
            query = "SELECT COUNT(r) FROM Reaction AS r WHERE r.employee_id = :logid AND r.report_id = :report_id"),
    @NamedQuery(//レポートに対するログイン社員のリアクションを取得
            name = "getLoginReaction",
            query = "SELECT r FROM Reaction AS r WHERE r.employee_id = :logid AND r.report_id = :report_id"),
    @NamedQuery(//レポートに対する全リアクション数を取得
            name = "getReactionsCount",
            query = "SELECT COUNT(r) FROM Reaction AS r WHERE r.report_id = :report_id"),
    @NamedQuery(//１社員の全レポート投稿に対してのトータルリアクション数を取得
            name = "getEmployeeReactionsCount",
            query = "SELECT COUNT(r) FROM Reaction AS r WHERE r.report_id.employee = :employee"),
})

@Entity
public class Reaction {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "type", nullable = false)
    private Integer type;

    @Column(name = "message", length = 255)
    private String message;

    @Column(name = "created_at",nullable = false)
    private Timestamp created_at;

    @ManyToOne
    @JoinColumn(name = "report_id" ,nullable = false)
    private Report report_id;

    @ManyToOne
    @JoinColumn(name = "employee_id" ,nullable = false)
    private Employee employee_id;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Timestamp getCreated_at() {
        return created_at;
    }

    public void setCreated_at(Timestamp created_at) {
        this.created_at = created_at;
    }

    public Report getReport_id() {
        return report_id;
    }

    public void setReport_id(Report report_id) {
        this.report_id = report_id;
    }

    public Employee getEmployee() {
        return employee_id;
    }

    public void setEmployee(Employee employee_id) {
        this.employee_id = employee_id;
    }

}
