@param String time
@param String email
@param String username
@param String resetLink
@param String ip
@param String agent

<!DOCTYPE html>
<html lang="en">
<body>
<p>Hi <strong>${username}</strong>,</p>
<p>Your account <strong>${email}</strong> was recently signed-in from a new location, device or browser.</p>

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

<p>
    <strong>If this was you</strong>
    <br>
    <span>You can ignore this message. There's no needed to take any action.</span>
</p>
<p>
    <strong>If this wasn't you</strong>
    <br>
    <span>Let <a href="${resetLink}"><strong>change your password</strong></a>. You'll be logged out of all your active <strong>Mini Ecommerce</strong> sessions except the one you're using at this time.</span>
</p>
</body>
</html>
