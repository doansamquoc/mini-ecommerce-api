@param String time
@param String username
@param String resetLink
@param String ip
@param String agent

<!doctype html>
<html lang="en">
<body>
<p>Hi <strong>${username}</strong>,</p>
<p>We've received a request to reset your password.</p>
<p>Click the link below to reset your password:</p>
<a href="${resetLink}">${resetLink}</a>
<p>The requested from</p>
<table style="width: 100%; border-collapse: collapse;">
    <tr>
        <td style="padding: 8px 0; width: 100px;">
            <strong>IP Address:</strong>
        </td>
        <td style="padding: 8px 0;">
            <span>${ip}</span>
        </td>
    </tr>
    <tr>
        <td style="padding: 8px 0;">
            <strong>Device:</strong>
        </td>
        <td style="padding: 8px 0;">
            <span>${agent}</span>
        </td>
    </tr>
    <tr>
        <td style="padding: 8px 0;">
            <strong>Time:</strong>
        </td>
        <td style="padding: 8px 0;">
            <span>${time}</span>
        </td>
    </tr>
</table>
<p>If this was mistake, just ignore this email and nothing will happen.</p>
</body>
</html>